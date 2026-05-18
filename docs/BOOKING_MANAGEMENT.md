# Booking Management for Room Operations

## Overview
This document explains how bookings are automatically managed when creating, updating, or deleting rooms.

## Room Lifecycle with Booking Management

### 1. Creating a Room ✅

**What happens:**
- Room is created with specified capacity (e.g., 4)
- `@PrePersist` lifecycle callback in `Room` entity auto-creates beds (1, 2, 3, 4)
- Each bed is created with:
  - `bedNumber`: 1-4
  - `room`: reference to parent Room
  - `tenantId`: from current context
  - `occupancyStatus`: AVAILABLE (default)
- **No bookings are created yet** (bookings are created when crews are assigned)

**Code path:**
```
RoomServiceImpl.createRoom()
  ↓ Sets tenantId
  ↓ roomRepository.save() → triggers @PrePersist
  ↓ Room.createBedsOnPersist() → auto-creates beds
```

### 2. Updating Room Capacity ✅

#### Adding Beds (capacity increase)
- New beds are created and added to the room
- No booking impact (bookings only exist when assigned)
- New beds start with status AVAILABLE

#### Reducing Beds (capacity decrease)
**Safety checks performed:**
1. Identifies beds to remove (from the end, highest bed numbers first)
2. For each bed to remove:
   - Checks if bed has **active bookings** (checkOutTime == null)
   - If yes → throws error with specific bed information
   - If no → proceeds
3. Deletes all non-active bookings for those beds
4. Removes beds from the room list
5. Orders cascading delete of beds (via orphanRemoval)

**Error example:**
```
Cannot reduce capacity: bed 3 has active bookings. 
Complete or cancel all bookings before reducing capacity.
```

**Code flow:**
```
RoomServiceImpl.updateRoom()
  ↓ If desiredCapacity < currentCount:
  ↓   - Identify beds to remove
  ↓   - Check for active bookings (existsByBed_IdAndCheckOutTimeIsNull)
  ↓   - If active found → REJECT with error
  ↓   - DELETE bookings via bookingRepository.deleteByBedId()
  ↓   - REMOVE beds from list
  ↓   - CASCADE deletes beds on roomRepository.save()
```

### 3. Deleting a Room ✅

**Safety checks performed:**
1. Fetches room and all its beds
2. For **each bed**, checks for **active bookings** (checkOutTime == null)
3. If any active bookings found:
   - Lists affected beds by bed number
   - Throws error with specific bed numbers
   - DELETE is aborted
4. If all clear:
   - Deletes ALL bookings (active and inactive) for all beds
   - Deletes room (cascade deletes beds)
   - Logs clean deletion

**Error example:**
```
Cannot delete room: The following beds have active bookings: 
Bed 1, Bed 3. Complete or cancel all bookings before deleting this room.
```

**Code flow:**
```
RoomServiceImpl.deleteRoom()
  ↓ Fetch room and beds
  ↓ For each bed:
  ↓   - Check for active bookings (existsByBed_IdAndCheckOutTimeIsNull)
  ↓   - Collect beds with active bookings
  ↓ If any active bookings exist:
  ↓   → REJECT with list of affected beds
  ↓ If clear:
  ↓   - For each bed: DELETE all bookings (bookingRepository.deleteByBedId)
  ↓   - roomRepository.delete(room) → CASCADE deletes beds
```

## Database Constraints

**Key relationships:**
```
Room (1) ←→ (Many) Bed
  - cascade = CascadeType.ALL
  - orphanRemoval = true
  - When Room deleted → all Beds deleted
  - When Bed removed from list → Bed deleted

Bed (1) ←→ (Many) Booking
  - No cascade on Booking side
  - Bookings manually deleted before Bed deletion
  - Prevents orphaned booking records
```

## Repository Methods

### BookingRepository additions:

```java
// Check if bed has active bookings (not checked out)
boolean existsByBed_IdAndCheckOutTimeIsNull(UUID bedId);

// Get all bookings for a bed
List<Booking> findAllByBed_Id(UUID bedId);

// Delete all bookings for a bed (used during capacity reduction/deletion)
void deleteByBedId(UUID bedId);

// Get bookings for multiple beds
List<Booking> findAllByBed_IdIn(List<UUID> bedIds);
```

## Error Scenarios

### ❌ Cannot reduce room capacity
```
Scenario: User tries to reduce room from 4 beds to 2 beds
Bed 3 has active booking (crew checked in, not checked out)
Bed 4 is available

Result: Error - cannot proceed
Message: "Cannot reduce capacity: bed 3 has active bookings..."
Action: User must check out crew on bed 3 before retrying
```

### ❌ Cannot delete room
```
Scenario: User tries to delete Room A
Bed 1: no bookings
Bed 2: active booking (crew checked in)
Bed 3: past bookings (all checked out)
Bed 4: no bookings

Result: Error - cannot proceed
Message: "The following beds have active bookings: Bed 2..."
Action: User must check out crew on bed 2 before retrying
```

### ✅ Can delete room
```
Scenario: Same room but crew on Bed 2 has now checked out

Result: Delete succeeds
Steps:
  1. All bookings for Beds 1-4 deleted (including past ones)
  2. All beds deleted (cascade)
  3. Room deleted
  4. Logs: "Room deleted successfully with all associated beds and bookings"
```

## Design Principles

1. **Transaction Safety**: All operations are @Transactional
2. **Active Booking Prevention**: Never allow deleting/reducing beds with active bookings
3. **Data Cleanup**: Delete non-active bookings during operations to prevent orphaning
4. **Clear Errors**: Messages specify which beds have issues
5. **Tenant Isolation**: All operations respect tenant filters via TenantContext
6. **Cascading Deletes**: Use JPA cascade + orphanRemoval for beds, manual booking cleanup for bookings

## Future Enhancements

- Add soft-delete option (mark as deleted instead of hard delete) for audit trails
- Add batch check-out capability before room deletion
- Add booking archival before hard deletion
- Add capacity change history tracking
- Add API endpoint to cancel all bookings for a bed/room before deletion

