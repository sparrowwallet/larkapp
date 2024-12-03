![Lark logo](https://github.com/sparrowwallet/larkapp/raw/refs/heads/master/larklogo.png)

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

Note that on Linux, udev rules [may need to be installed](https://github.com/sparrowwallet/lark/blob/master/src/main/resources/udev/README.md) if you are not using the .deb/.rpm installation package.

#### macOS
```shell
> Lark.app/Contents/MacOS/Lark
```

#### Windows
```shell
> lark/lark.exe
```

### Example usage

Show usage:

```shell
lark --help
```

Enumerate all connected hardware wallets

```shell
lark enumerate
```

Get Native Segwit xpub

```shell
lark --device-type trezor getxpub "m/84'/0'/0'"
```

Sign a testnet transaction

```shell
lark --chain test --device-type coldcard signtx "cHNid..."
```

Sign a multisig transaction, supplying a previous wallet registration

```shell
lark --device-type ledger --wallet-desc "wsh(sortedmulti(2,[2811576b/48h/0h/0h/2h]xpub...,[6533280b/48h/0h/0h/2h]xpub...))" --wallet-name "2 of 2 Multisig" --wallet-registration "c0ee51bae9b3e5ee5a5e01dbcd336cd5c5f8a5b760d1c68f6a2a0b91c2580406" signtx "cHNid..."
```

### Running from source

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

Other packages may also be necessary to build depending on the platform. On Debian/Ubuntu systems:

`sudo apt install -y rpm fakeroot binutils`

The Lark binaries can be built from source using

`./gradlew jpackage`

When updating to the latest HEAD

`git pull --recurse-submodules`

The release binaries are reproducible (pre codesigning and installer packaging).


