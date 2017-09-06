#!/usr/bin/env bash

host="${1:-http://127.0.0.1:9000}"
opts="-v"

do_curl() {
    prefix=$1

    curl "$opts" "$host/$prefix/call"
    curl "$opts" "$host/$prefix/future"
    curl "$opts" "$host/$prefix/http"
    curl "$opts" "$host/$prefix/find"

    curl "$opts" "$host/$prefix/call?trace=true"
    curl "$opts" "$host/$prefix/future?trace=true"
    curl "$opts" "$host/$prefix/http?trace=true"
    curl "$opts" "$host/$prefix/find?trace=true"
}

do_curl "play/import"
do_curl "play/inject"
do_curl "scala"
do_curl "akka"
do_curl "akka/fork-join"
do_curl "akka/thread-pool"
do_curl "manual/thread-pool"

exit 0
