# Lark App

The Lark application is a command line app for interacting with USB hardware wallets in Bitcoin related functions. 
It uses the Lark Java library at https://github.com/sparrowwallet/lark, which in turn is a port of the Python library [HWI](https://github.com/bitcoin-core/HWI).
The Lark command line application is designed to be a drop-in replacement for HWI, with a subset of commands implemented.
Documentation on the commands is available by running it with `--help`. 

## Running

Depending on your operating system, once extracted/installed, Lark can be run from the command line as follows: 

#### Linux
```shell
> lark/bin/lark
```

#### macOS
```shell
> Lark.app/Contents/MacOS/Lark
```

#### Windows
```shell
> lark/lark.exe
```

### Example usage

Lark

```shell
lark --help
```

```shell
lark enumerate
```

If you prefer to run Lark directly from source, it can be launched from within the project directory with

`./runlark`

Java 22 or higher must be installed.


## Building

To clone this project, use

`git clone --recursive git@github.com:sparrowwallet/larkapp.git`

or for those without SSH credentials:

`git clone --recursive https://github.com/sparrowwallet/larkapp.git`

In order to build, Lark requires Java 22 or higher to be installed.
The release binaries are built with [Eclipse Temurin 22.0.2+9](https://github.com/adoptium/temurin22-binaries/releases/tag/jdk-22.0.2%2B9).

The Lark binaries can be built from source using

`./gradlew jpackage`

When updating to the latest HEAD

`git pull --recurse-submodules`

The release binaries are reproducible (pre codesigning and installer packaging).


