#!/usr/bin/env bash

port=9000
opts="-v"

curl "$opts" "http://127.0.0.1:$port/play/import"
curl "$opts" "http://127.0.0.1:$port/play/import/future"
curl "$opts" "http://127.0.0.1:$port/play/import/http"

curl "$opts" "http://127.0.0.1:$port/play/inject"
curl "$opts" "http://127.0.0.1:$port/play/inject/future"
curl "$opts" "http://127.0.0.1:$port/play/inject/http"

curl "$opts" "http://127.0.0.1:$port/scala"
curl "$opts" "http://127.0.0.1:$port/scala/future"
curl "$opts" "http://127.0.0.1:$port/scala/http"

curl "$opts" "http://127.0.0.1:$port/akka"
curl "$opts" "http://127.0.0.1:$port/akka/future"
curl "$opts" "http://127.0.0.1:$port/akka/http"

curl "$opts" "http://127.0.0.1:$port/akka/fork-join"
curl "$opts" "http://127.0.0.1:$port/akka/fork-join/future"
curl "$opts" "http://127.0.0.1:$port/akka/fork-join/http"

curl "$opts" "http://127.0.0.1:$port/akka/thread-pool"
curl "$opts" "http://127.0.0.1:$port/akka/thread-pool/future"
curl "$opts" "http://127.0.0.1:$port/akka/thread-pool/http"

curl "$opts" "http://127.0.0.1:$port/manual/thread-pool"
curl "$opts" "http://127.0.0.1:$port/manual/thread-pool/future"
curl "$opts" "http://127.0.0.1:$port/manual/thread-pool/http"

exit 0
