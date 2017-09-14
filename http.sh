#!/usr/bin/env bash

host="${1:-http://127.0.0.1:9000}"
opts="-v"

do_curl() {
    prefix=$1

    do_run() {
        curl "$opts" "$host/$prefix/call?$1"
        curl "$opts" "$host/$prefix/future?$1"
        curl "$opts" "$host/$prefix/http?$1"
        curl "$opts" "$host/$prefix/find?$1"
    }

    do_run ""
    do_run "trace=true"
}

do_curl "play/import"
do_curl "play/inject"
do_curl "scala"
do_curl "akka"
do_curl "akka/fork-join"
do_curl "akka/thread-pool"
do_curl "manual/thread-pool"

exit 0
