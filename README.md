# web1

A tiny web project example written in Clojure


## Prepare

### Run PostgreSQL

````
docker run -v $(pwd)/data:/var/lib/postgresql/data --net=host -e POSTGRES_DB=db1 postgres:12
````

### Run Clojure docker

````
docker run --rm -it -u 1000 --net=host -v $(pwd):/work -e HOME=/work -w /work clojure:tools-deps
````

### Migrate DB

```
clojure -m web1.migrate
```

## Run

````
clojure -A:run-server
````

## Try

### Basic

````
http://localhost:3000
````

### Add item

By HTTPie

````
echo '{"metadata":{"title":"meta1"},"id":80}' | http post localhost:3000/api/meta
````

### List items

By HTTPie

````
http get localhost:3000/api/meta
````

## Create migration

````
clojure -m web1.create-migration <name>
````