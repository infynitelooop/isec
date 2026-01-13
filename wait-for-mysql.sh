#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

echo "Waiting for MySQL at $host..."

until mysql -h "$host" \
  -u"$SPRING_DATASOURCE_USERNAME" \
  -p"$SPRING_DATASOURCE_PASSWORD" \
  -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done

echo "MySQL is ready!"
exec $cmd
