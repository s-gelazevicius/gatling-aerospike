# Gatling-Aerospike

An unofficial [Gatling](http://gatling.io/) 2.5 stress test plugin
for [Aerospike](http://aerospike.com/).

Inspired by [gatling-kafka](https://github.com/mnogu/gatling-kafka).

This plugin supports the Aerospike put and get operations.

## Usage

### Creating a jar file

Install [sbt](http://www.scala-sbt.org/) 0.13 if you don't have.
And create a jar file:

    $ sbt assembly

If you want to change the version of Gatling used to create a jar file,
change the following line in [`build.sbt`](build.sbt):

```scala
"io.gatling" % "gatling-core" % "2.2.3" % "provided"
```

and run `sbt assembly`.

If you don't want to include aerospike-client library to the jar file,
change a line on aerospike-client in [`build.sbt`](build.sbt) from

```scala
"com.aerospike" % "aerospike-client" % "3.3.4"
```

to

```scala
"com.aerospike" % "aerospike-client" % "3.3.4" % "provided")
```

and run `sbt assembly`.

### Putting the jar file to lib directory

Put the jar file to `lib` directory in Gatling:

    $ cp target/scala-2.11/gatling-aerospike-assembly-*.jar /path/to/gatling-charts-highcharts-bundle-2.2.*/lib

If you edited `build.sbt` in order not to include aerospike-client library
to the jar file, you also need to copy aerospike-client library to `lib` directory:

    $ cp /path/to/aerospike-client-*.jar /path/to/gatling-charts-highcharts-bundle-2.2.*/lib


###  Creating a simulation file

    $ cd /path/to/gatling-charts-highcharts-bundle-2.2.*
    $ vi user-files/simulations/AerospikeSimulation.scala

You can find sample simulation files in the [test directory](src/test/scala/com/github/simsasg/gatling/aerospike/test).
Among these files, [BasicSimulation.scala](src/test/scala/com/github/simsasg/gatling/aerospike/test/BasicSimulation.scala) would be a good start point.
Make sure that you replace `BasicSimulation` with `AerospikeSimulation` in `BasicSimulation.scala`
if your simulation filename is `AerospikeSimulation.scala`.

### Running a stress test

After starting an Aerospike server, run a stress test:

    $ bin/gatling.sh

## License

Apache License, Version 2.0
