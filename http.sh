#!/usr/bin/env bash

port=9000
opts="-I"

curl "$opts" "http://127.0.0.1:$port/play/import"
curl "$opts" "http://127.0.0.1:$port/play/import/nested"

curl "$opts" "http://127.0.0.1:$port/play/inject"
curl "$opts" "http://127.0.0.1:$port/play/inject/nested"

curl "$opts" "http://127.0.0.1:$port/scala"
curl "$opts" "http://127.0.0.1:$port/scala/nested"

curl "$opts" "http://127.0.0.1:$port/akka"
curl "$opts" "http://127.0.0.1:$port/akka/nested"

curl "$opts" "http://127.0.0.1:$port/akka/fork-join"
curl "$opts" "http://127.0.0.1:$port/akka/fork-join/nested"

curl "$opts" "http://127.0.0.1:$port/akka/thread-pool"
curl "$opts" "http://127.0.0.1:$port/akka/thread-pool/nested"

curl "$opts" "http://127.0.0.1:$port/manual/thread-pool"
curl "$opts" "http://127.0.0.1:$port/manual/thread-pool/nested"

exit 0
