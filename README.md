# Advent of Code 2022

[Advent of Code](https://adventofcode.com/2022/) is a set of small yearly programming problems for a variety of skill levels. They can be solved in any programming language. I used Kotlin with Gradle for this year's project.


### What's Included?
Complete solutions for both parts of all 25 days.
### Running the Code

Clone the repository and run using the local gradle wrapper, or build a jar and run the jar via command line. You'll need to pass arguments to the run command for the day you want to run. You can run multiple days at once. 

Examples:
```sh
./gradlew run --args "day1"
./gradlew run --args "day1 day2"
```

Or using a fat jar:
```sh
./gradlew clean build
java -jar ./build/libs/advent2022-0.1.0.jar day1 day2
```

# License
BSD-3 Clause License

Copyright 2022 Jacob Kanipe-Illig

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.