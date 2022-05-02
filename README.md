# physical-system-simulation-algorithms

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.6513032.svg)](https://doi.org/10.5281/zenodo.6513032)

## description
- Simple physical system simulation algorithms for sytems like cloth, rigid and deformable objects, shallow water.
- Cloth and deformable objects are simulated with a grid of point masses connected by springs.
- Eulerian integration and Midpoint method are used to solve force equations and get positions to animate the simulation.
- Shallow water simulation implements Lax-Wendroff method to numerically solve very simplified version of Navier-Stokes equations.
## roadmap
- Problems solved until now are listed in demonstration section.
## code
- Code is written in Java, should work with JRE 8+.
    - `src/` contains all source code.
    - `jars/` contain all libraries bundled as jars.
        - `processing` is used as a rendering library.
        - `queasy cam` is used as a camera library.
    - `data/` contains resources such as images, obj, mtl files.
    
### references
- [Perpendicular distance of a given point from line segment.](https://stackoverflow.com/questions/4858264/find-the-distance-from-a-3d-point-to-a-line-segment/36425155#36425155)
- [Method to apply texture to cloth.](https://www.youtube.com/watch?v=JunJzIe0hEo)
- [2D shallow water simulation equations.](https://www.youtube.com/watch?v=TviBQrb74Xc)
- [Water texture.](http://www.google.com/url?q=http%3A%2F%2Fseamless-pixels.blogspot.com%2F2012%2F10%2Ftileable-water-texture.html&sa=D&sntz=1&usg=AFQjCNHwL1JZi2-tC3kvSe1fl4y3ziekKw)
- [Aladdin Magic Carpet texture.](https://www.google.com/url?q=https%3A%2F%2Fwww.pinterest.es%2Fpin%2F804948133380274506%2F&sa=D&sntz=1&usg=AFQjCNEjyoLbs-ab1ULplapMTYS0jgvf5Q)
- [Mario (.obj) model.](https://www.turbosquid.com/3d-models/free-mario-bros-3d-model/621837)
- Grumpy cat, parachute, jelly textures from google images.

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

### Basic cloth as a grid connected spring-mass system
#### videos
[![](http://img.youtube.com/vi/tKMVqgpqZCo/0.jpg)](https://www.youtube.com/watch?v=tKMVqgpqZCo)

- 3D Realtime rendering.
- User-controlled first person camera.
- Texture.

### Cloth falling on sphere (1-way interaction)
#### videos
[![](http://img.youtube.com/vi/96CpGuKXJ-g/0.jpg)](https://www.youtube.com/watch?v=96CpGuKXJ-g)

### Air drag and wind effects
#### videos
[![](http://img.youtube.com/vi/XDbq-ENKS0E/0.jpg)](https://www.youtube.com/watch?v=XDbq-ENKS0E)

- Billowing wind effect simulated with aerodynamic drag @ `0:53`.

### Rigid sphere falling on cloth (2-way interaction)
#### videos
[![](http://img.youtube.com/vi/J1tGb_0aIio/0.jpg)](https://www.youtube.com/watch?v=J1tGb_0aIio)

- The cloth shoots the sphere like a slingshot when released @ `00:20`.

#### images
| | |
| --- | --- |
| ![](./github/ball_on_cloth/1.jpg) | ![](./github/ball_on_cloth/2.jpg) |

### Rigid circles falling on a thread (2-way interaction)
#### videos
[![](http://img.youtube.com/vi/xcqUPiIVJ4U/0.jpg)](https://www.youtube.com/watch?v=xcqUPiIVJ4U)

### Rigid spheres falling on a cloth (2-way interaction)
#### videos
[![](http://img.youtube.com/vi/bSrS7RrnmjY/0.jpg)](https://www.youtube.com/watch?v=bSrS7RrnmjY)

### Effect of skip node springs on cloth stiffness
#### videos
| No skip nodes | Yes skip nodes |
| --- | --- |
| [![](http://img.youtube.com/vi/JGXYBOnRtmQ/0.jpg)](https://www.youtube.com/watch?v=JGXYBOnRtmQ) | [![](http://img.youtube.com/vi/PhURLD3SPFU/0.jpg)](https://www.youtube.com/watch?v=PhURLD3SPFU) |
| @ `00:29` (wind speed = 33) billowing waves are flattened | @ `00:25` (wind speed = 33) billowing waves are pronounced |
| @ `00:33` (wind speed = 46) more elongated and tears off | @ `00:48` (wind speed = 46) less elongated and is intact. |

### Tearable cloth
- The sword is attached to camera's position and locked to its orientation in  ground XZ plane
#### videos

[![](http://img.youtube.com/vi/N0447hrrQ8c/0.jpg)](https://www.youtube.com/watch?v=N0447hrrQ8c)

#### images
| | |
| --- | --- |
| ![](./github/tearable_cloth/1.jpg) | ![](./github/tearable_cloth/2.jpg) |
| ![](./github/tearable_cloth/3.jpg) | ![](./github/tearable_cloth/4.jpg) |

- Cloth tearing due to high speed winds @ `00:38`
#### videos

[![](http://img.youtube.com/vi/rDReMugxMj4/0.jpg)](https://www.youtube.com/watch?v=rDReMugxMj4)

#### images
| | | |
| --- | --- | --- |
| ![](./github/wind_tear/1.jpg) | ![](./github/wind_tear/2.jpg) | ![](./github/wind_tear/3.jpg)

### Burnable cloth
- Hot ball starts fire on cloth at arbitrary points
#### videos
[![](http://img.youtube.com/vi/HhDb_luKqAY/0.jpg)](https://www.youtube.com/watch?v=HhDb_luKqAY)

#### images
| | | |
| --- | --- | --- |
| ![](./github/burning_cloth/1.jpg) | ![](./github/burning_cloth/2.jpg) | ![](./github/burning_cloth/3.jpg)
| ![](./github/burning_cloth/4.jpg) | ![](./github/burning_cloth/5.jpg) | ![](./github/burning_cloth/6.jpg)
| ![](./github/burning_cloth/7.jpg) | ![](./github/burning_cloth/8.jpg) | ![](./github/burning_cloth/9.jpg)
| ![](./github/burning_cloth/10.jpg) | |

### Parachute shooting
- Air drag slows down the sky diver.
- A hot ball shot at parachute burns it down thus making her free fall.

#### videos
[![](http://img.youtube.com/vi/cl9NXG5Waa0/0.jpg)](https://www.youtube.com/watch?v=cl9NXG5Waa0)

#### images
| | |
| --- | --- |
| ![](./github/parachute/1.jpg) | ![](./github/parachute/2.jpg) |

### Deformable objects
- Falling cat
#### videos

[![](http://img.youtube.com/vi/rD8QPUcVjRM/0.jpg)](https://www.youtube.com/watch?v=rD8QPUcVjRM)

#### images
| | | |
| --- | --- | --- |
| ![](./github/falling_cat/1.jpg) | ![](./github/falling_cat/2.jpg) | ![](./github/falling_cat/3.jpg)
| ![](./github/falling_cat/4.jpg) | ![](./github/falling_cat/5.jpg) | ![](./github/falling_cat/6.jpg)
| ![](./github/falling_cat/7.jpg) | ![](./github/falling_cat/8.jpg) | ![](./github/falling_cat/9.jpg)

- Cutting jelly
#### videos

[![](http://img.youtube.com/vi/m2qMO76Vf7Y/0.jpg)](https://www.youtube.com/watch?v=m2qMO76Vf7Y)

### 1D shallow water
#### videos
[![](http://img.youtube.com/vi/ZMiD5p3hyFg/0.jpg)](https://www.youtube.com/watch?v=ZMiD5p3hyFg)

### 2D shallow water
- The object used is used to display the disturbance we are creating in water columns underneath it.
- It creates vibrations (multiple disturbances) causing multiple waves.
#### videos
[![](http://img.youtube.com/vi/xNPCHXDTPGM/0.jpg)](https://www.youtube.com/watch?v=xNPCHXDTPGM)
[![](http://img.youtube.com/vi/jCtxuBTTckk/0.jpg)](https://www.youtube.com/watch?v=jCtxuBTTckk)

#### images
| | | |
| --- | --- | --- |
| ![](./github/shallow_water/1.jpg) | ![](./github/shallow_water/2.jpg) | ![](./github/shallow_water/3.jpg)

### Comparing integrators
- The integrator is toggled interactively (by pressing num 2) in this demo.
- The integrator used for that instant is shown in the title bar.
- Eularian integrator is faster, but less stable especially in high winds.
- Higher order integrator is slower, but more stable even in high winds.
- The better integrator is not exactly second order but a version in between first and second order. However we want to present it as it gives better stability in this animation.
#### videos
[![](http://img.youtube.com/vi/47O2DB-IKyQ/0.jpg)](https://www.youtube.com/watch?v=47O2DB-IKyQ)
