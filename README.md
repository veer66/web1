# web1

A tiny web project example written in Clojure


## Prepare

````
docker run --rm -it -u 1000 --net=host -v $(pwd):/work -e HOME=/work -w /work clojure:tools-deps
````

## Run

````
clojure -A:run-server
````

## Try

````
http://localhost:3000
````