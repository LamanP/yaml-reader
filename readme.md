# yaml-reader
This project contains a facade API that is built on snake yaml to easily explore YAML resources.
The basic snake yaml simply returns an `Object` after parsing, which needs to be type cast to
the expected `Map<?,?>` for an object, or `List<?>` for arrays. This leads to ugly code.

__yaml-reader__ provides a clean interface to access the data. Apart from simpy reading or iterating
objects/array, it also provides some search functionality.

## Get the code
The source code can be obtained from [Github](https://github.com/LamanP/yaml-reader). Use the
pom.xml to get the dependencies.

## Basic usage
0. Start with `YAMLFactory` to parse YAML from either a stream, reader or string.
0. Each property or array element within the structure has a unique *address*, which is the path from the root to the element, containing. Path elements are either property names (objects), or array indexes (arrays) and they are separated by slashes.

## Basic interfaces
|Interface|Description|
|---|---|
|IYAMLArtifact|Represents a YAML *artifact*, that is either an object or an array.|
|IYAMLArray|Represents an array|
|IYAMLObject|Represents an object|
|IYAMLArrayElementProcessor|Client-provided interface to process all iterated elements in an array|

## Examples
Given the following YAML resource:

```
---
str: string1
long: 85
bool: true
nothing:
ar:
- string2
- 86
- false
- str: string3
  long: 87
```

This is an object that can be represented by a `IYAMLObject`.

Here are some examples of calls and their return values. The variable `yr` is supposed to represent this object.
It is declared as follows:

```
IYAMLObject yo;
```

|Call|Return value|Comments|
|---|---|---|
|`yo.ob("str")`|"string1"| |
|`yo.ob("err")`|-|Throws a YAMLException, as the "err" property does not exist|
|`yo.oob("err")`|null|The first *o* in *oob* means *optional*. We also have `ar()` and `oar()`|
|`yo.str("ar",0)`|"string3"|Array indices can be specified as part of an address|