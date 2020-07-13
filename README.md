# physical-systems-simulation-algorithms

## description
- Simple physical system simulation algorithms for sytems like cloth, rigid and deformable objects, water, fire.
## roadmap
- Problems solved until now are listed in demonstration section.
## code
- Code is written in Java, should work with JRE 8+.
    - `src/` contains all source code.
    - `jars/` contain all libraries bundled as jars.
        - `processing` is used as a rendering library.
        - `queasy cam` is used as a camera library.
    - `data/` contains resources such as images, obj, mtl files.
## documentation
- For most of the code, the documentation is itself.
## usage
- Open a terminal at project root (the directory containing this file).
- Use `javac -cp "jars/*" -d build/ $(find -name "*.java")` to compile and put all output class files under `build/`.
- Use `java -cp "build/:jars/*" <package>.<to>.<path>.<class>` to run any simulation.
    - For example `java -cp "build/:jars/*" demos.BasicCloth`.
- Common controls
    - `w a s d` for basic camera movements.
    - `q e` for camera up and down movements.
- Tested on Ubuntu 18.04
    - If you use a distrubution that uses rolling release cycle (like Arch) you might have to install some older version of JRE and mesa (opensource intel openGL driver) that work with processing library.
## demonstration
- The title bar shows the state of the system at any instant.
- The videos demos are compiled into a [playlist](https://www.youtube.com/playlist?list=PLrz4CUP15JSI6OnuyqtFs-BR5opYcNjvx).

### Thread as serially connected spring-mass system
#### videos
[![](http://img.youtube.com/vi/IbJ_Yum13rU/0.jpg)](https://www.youtube.com/watch?v=IbJ_Yum13rU)
- Each rope is made of a series of 3 massless-springs and 3 point-masses.
- Forces acting on each mass are:
        - Gravity.
        - Restoration force from each spring along the length of spring.
        - Dampening force from each spring along the length of spring.
- The point-masses in thread in the middle have lesser mass and hence they reach equilibrium at shorter lengths.

#### images
| ![](./github/threads/1.jpg) | ![](./github/threads/2.jpg) | ![](./github/threads/3.jpg) |
| --- | --- | --- |
| Initial state, released at horizontal position | Intermediate state, in motion | Equilibrium state, settled at vertical position |

# Things used
- https://stackoverflow.com/a/36425155/4031302 for distance from point to line segment
