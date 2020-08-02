Mower
=============


<a href="https://typelevel.org/cats/"><img src="https://typelevel.org/cats/img/cats-badge.svg" height="40px" align="left" alt="Cats friendly" /></a><br>

<br>

The technology used here are `scala 2.13`, `cats` and `cats effects`.

## Requirements
- Java installed (>= 1.8)
- sbt client installed or bloop client

## Tests

To run unit tests using `sbt`:

```
sbt test
```
or using `bloop`
```
bloop test tests
```

## Run

To run using `sbt`:

```
sbt "; project core; run"
```
or using `bloop`
```
bloop run core
```

## Build Docker image
```
sbt docker:publishLocal
```

Our image should now be built. We can check it by running the following command:

```
> docker images | grep mower
REPOSITORY                    TAG                 IMAGE ID            CREATED             SIZE
mower                 latest              646501a87362        2 seconds ago       100MB
```
To run our application using our Docker image, run the following command:

```
cd /app
docker-compose up
```

## Code Insight
The application is written in tagless final style, it means we abstract
over the effect type `F[_]`.
This will ease the code maintenance in case of 
effects type changement(`cats.IO`, `monix.Task`, or any equivalent data structure)

This code design choice adds complexity, but in the other hand,
we have a fully functional solution, strongly typed, thus,
we gain a lot of flexibility to manage conccurrent computation, 
and simplify the tests writing.

PS: `IO` Effect from `cats-effect` is used at runtime
### Simplified execution
On application bootstrap, we load resource file under `resources/input.txt`, in case of success:

- Generate the initial problem description
- Call solution solver:
    - build the initial `lawn`
    - switch the context to the dedicated thread pool
    - run `solve` algorithm on each `Mower` and its `commands` in a seperate fiber.
        (https://typelevel.org/cats-effect/datatypes/fiber.html)
        The output is the final `Mower` position and direction;
    - MVar are used to hold the state of the `Lawn`, Mvars ensure thread-safe mutable variables
    (https://typelevel.org/cats-effect/concurrency/mvar.html);
    - Run in parallel all the fibers, wait for them to complete the task and return the solution.
- Tests are written as Property based testing, all the tests objects are automatically generated, the aim is to find properties that can be validated (identity in parsing, identity in moving ..)     
- Running different JVM threads for relatively small tasks generate computation overhead due to the context shift.
Having in mind that the JVM threads map directely to the OS native threads, I chose to use `fork/join` operations on Fibers (or green thread), in a seperate thread pool to not block the main thread, and to have unlimited number of green threads.

## Improvements points

- Validate that multiple mowers can't be on the same initial position;
- Property based testing is a great tool to validate invariable properties, though, add 
unit tests to improve the whole coverage.

## Contact
You can ping me if you need any information.<br>
email: `hajlaoui.nader@gmail.com`


Good review !