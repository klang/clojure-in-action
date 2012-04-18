# Chapter 12 - the part about redis

## redis installation

klang@endjinn:~/projects/redis-2.4.9$ make 32bit
klang@endjinn:~/projects/redis-2.4.9$ make test

## redis startup

klang@endjinn:~/projects/redis-2.4.9$ ./src/redis-server

## redis interaction

klang@endjinn:~/projects/redis-2.4.9$ ./src/redis-cli 
redis 127.0.0.1:6379> get "##14##:merchant-id"
"14"
redis 127.0.0.1:6379> llen "##14##:cart-items"
(integer) 4
redis 127.0.0.1:6379> lrange "##14##:cart-items" 0 4
1) "{\"sku\":\"XYZ\",\"cost\":22.4}"
2) "{\"sku\":\"ABC\",\"cost\":10.95}"
3) "{\"sku\":\"XYZ\",\"cost\":22.4}"
4) "{\"sku\":\"ABC\",\"cost\":10.95}"
redis 127.0.0.1:6379> exists "##14##:cart-items"
(integer) 1
redis 127.0.0.1:6379> 

## clojure startup

klang@endjinn:~/projects/clojure-in-action/redis$ lein swank

compile chapter12.core and try the s-expressions in the comment

