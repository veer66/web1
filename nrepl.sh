#!/bin/sh
clojure -Sdeps '{:deps {cider/cider-nrepl {:mvn/version "0.23.0-SNAPSHOT"}}}' \
    -m nrepl.cmdline \
    --middleware "[cider.nrepl/cider-middleware]"
