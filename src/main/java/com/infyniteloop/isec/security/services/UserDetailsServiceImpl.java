package com.infyniteloop.isec.security.services;


import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.isec.security.repository.UserRepository;
import com.infyniteloop.isec.security.services.impl.UserDetailsImpl;
import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;
import com.infyniteloop.runningroom.runningroom.repository.RunningRoomRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RunningRoomRepository runningRoomRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, RunningRoomRepository runningRoomRepository) {
        this.userRepository = userRepository;
        this.runningRoomRepository = runningRoomRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        RunningRoom runningRoom = runningRoomRepository.findById(user.getTenantId())
                .orElseThrow(() -> new UsernameNotFoundException("User is not associated with any Running Room " + username));
        return UserDetailsImpl.build(user, runningRoom);
    }




}
