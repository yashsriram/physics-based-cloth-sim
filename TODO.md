Physically-Based Simulation
Due: Feb 27

Check-In: Sunday, Feb 16
Overview: Last assignment, we simulated fuzzy phenomena using dense systems of
loosely interacting particles that followed some procedural set of rules for how they
evolved over time. The goal of this assignment is to simulate various systems that
follow well-defined physical laws. In the process, you should gain familiarity with
different techniques commonly used to animate complex physical phenomena, such
as differential equations, numerical integration, and spatial data-structures.

Requirements: Write (at least) two physically-based simulation demos. One, a
simulation of cloth interacting with an object. The second, can be a significantly
more advanced cloth simulation, a fluid simulation, sound simulation, or deformable
or rigid-body dynamics. You may work with a partner, have one partner turn in the
full assignment and the other submit a note saying who they worked with.

Check-in: There is mandatory mid-way check-in. For this, you must turn in a
webpage with a video (& code) of a simulated thread (or several threads) of cloth.
The thread(s) must be anchored at the top and allowed to dangle free on the bottom.
Note, for the check-in, we only need the vertical threads – adding horizontal (cross)
threads is optional, they will make the simulation less stable, so only try it second.

Demo ideas
- [ ] Cloth
    - [x] Ball falling on cloth
    - [x] Balls falling on cloth
    - [x] Air drag vs no air drag
    - [x] Wind tear (with and without skip nodes, with several fixed point configurations)
    - [x] Cloth falling on ball
    - [x] Skip nodes vs no skip nodes
    - [ ] Parachutes and burning cloth using sphere
    - [ ] Tear cloth by sword
    - [ ] Compare integrators

Bugs
- [x] separate acc calc and pos, vel updates in grid and series systems

Strongly Suggested Features
 - [x] (5) Realtime rendering (must document framerate)
 - [x] (5) 3D rendering, with user-controlled camera (must support first-person style)
 - [ ] (5) Real-time user interaction with system

Cloth-Object Interaction (1-way coupling object-to-cloth) [up to 40 points]
 - [x] (10) 2D Mass-spring cloth simulation
 - [x] (10) 3D Mass-spring cloth simulation [cumulative over 2D]
 - [x] (10) Drag-terms (must include demo showing effect of drag)
 - [x] (10) 1-way cloth-object interaction (extra points for non-spherical objects)

Cloth Performance Benchmarks (cumulative)
 - [x] (5) 15x15 Cloth with fast, smooth motion at 20 FPS
 - [x] (5) 20x20 Cloth with fast, smooth motion at 30 FPS
 - [x] (5) 30x30 Cloth with fast, smooth motion and obstacle interaction at 30 FPS
If your cloth moves slowly you will get no credit for performance benchmarks

General Features
Integrator
- [x] (5) Eulerian (1st order)
- [ ] (5) Higher-order Explicit (e.g., Midpoint, RK4, Lax–Wendroff)
- [ ] (10) Implicit Integrator
- [ ] (5) Compare two or more integration methods in terms of speed & stability

Rendering
- [x] (10) Textured simulated objects (e.g., textured cloth)

Acceleration
- [ ] (5) Thread-parallel implementation (must document performance gain)
- [ ] (10) SIMD / GPU implementation (must document performance gain)
- [ ] (20) Spatial-data structure – must show a performance improvement

Additional Features
- [x] (10) Two-way coupling object-simulation coupling (e.g., cloth moves a ball)
- [ ] (10) Integrate 2D rigid body sphere-sphere interactions for several spheres
- [x] (20) Integrate 3D rigid body sphere-sphere interactions for several spheres
- [ ] (20) Combine (3D) rotational rigid body dynamics with another simulation

Advanced Cloth
- [x] (5) Billowing/blowing wind effect simulated with aerodynamic drag
- [x] (5) Skip nodes spring/threads to stiffen cloth (must show comparison)
- [ ] (10) Tear-able cloth.
- [ ] (15) Burnable cloth (should have particle effects for full credit)
- [ ] (20) Self-collision in cloth
- [ ] (40) Finite Element Method (FEM)-based cloth simulation

Water/Fluid Simulation
- [ ] (10) 1D Water surface, with shallow water equation
- [ ] (10) 2D Water surface, with shallow water equation [cumulative over 1D]
- [ ] (30) 2D Eulerian fluid simulation (e.g. Stam GDC ’03)
- [ ] (50) 3D Eulerian fluid simulation

2D Water/Fluid Performance Benchmarks (cumulative)
- [ ] (5) 50x50 Shallow Water Sim. at 20 FPS
- [ ] (5) 100x100 Shallow Water Sim. at 30 FPS
- [ ] (5) 100x100 Eulerian Fluid at 20 FPS
- [ ] (5) 200x200 Eulerian Fluid at 30 FPS
If your water moves slowly you will get no credit for performance benchmarks

Hair Simulation
- [ ] (20) 2D hair simulation (can’t miss collisions between strands)
- [ ] (20) 3D hair simulation (can’t miss collisions between strands) [cumulative over 2D]
- [ ] (10) Angle-based dynamics (must document effect)

Deformable Objects
- [ ] (20) 2D deformable objects (must show rotational effects)
- [ ] (10) 3D deformable objects (must show rotational effects) [cumulative over 2D]

Rigid-Body Dynamics w/ Rotation
- [ ] (30) Make 2D rotational rigid body dynamics simulation (10+ non-circle objects)
- [ ] (60) Make 3D rotational rigid body dynamics simulation (5+ non-sphere objects)

More things to simulate
- [ ] (30) Physical simulated (polyphonic) music instrument with user interaction

Art Contest: 5 points for honorable mentions, the winner gets 10 points.

Scoring
Partial credit will be given. Scores computed as follows (points above 100 possible):
-Undergraduate: Grade is √(totalPoints * 100) [e.g., 100 points will be full credit]
-Grad students: Grade is √(totalPoints * 84) [e.g., 120 points will be full credit]

Use of other code and tools
Anything you are getting credit for must be code you wrote specifically for this
course, clearly specify any external libraries you are using.

What to turn in
You must make a submission webpage with:
• Images & video of your systems
• A brief description of the features of your implementation
• Code you wrote
• List of the tools/library you used
• Brief write-up explaining difficulties you encountered
• Submission for the art contest (optional)
Remember, if you don’t tell us about it, we can’t give you credit for it.

Hints
-Use small timesteps
-There is a lot flexibility here. If you aren’t sure what to do, go for a cloth simulation
that interacts with a user-controlled ball. Use a spatial data-structure to accelerate cloth-ball collision detection.
-Make your simulations stable using just a few elements before you try highly detailed cloth or water.
-There are both easy paths and hard paths through the assignment. Don’t let it become too big of a time sink, but do try to have fun with it.
-Use a small timestep!
-Show off your system doing something interesting (e.g., let your cloth detach and fall onto something). It’s easier to give good grades to nice simulations. =)

Simulation Grading Guide
Cloth-Object Interaction (one way coupling object-to-cloth)
A-level (35-40 points):
The cloth falls smoothly to its resting position and moves at a natural speed. The
motion of the cloth is smooth, and it naturally drapes over an object in the scene.
The effect of the aerodynamic drag term are clear in the cloth’s motion.
B-level (30-35 points):
The cloth looks a bit like it’s moving in “slow-mode”, but the behavior is otherwise
smooth and stable. The cloth is affected by the object, but the motion at times looks
a little unnatural (maybe the motion is slightly bumpy, or there are overly big gaps
between the cloth and the object).
C-level (20-25 points):
The cloth falls down due to gravity, but it does not consistently fall in a stable path
to its resting state. The cloth’s interaction with the obstacles in the scene is unstable
or unnatural.
Additional Features (various points):
This simulation is a natural point to add mouse-based user interaction by letting
them control the ball/object that the cloth interacts with. Additionally, your cloth
simulation can likely be speed up through a parallelized implementation (which is
both points on its own and might increase your performance benchmarks). You may
also want to try texturing the cloth to look more visually interesting. Once you have
Eulerian integration working, you may want to try midpoint (or RK4) to see if you
can get a larger timestep.
Advanced Cloth [various points]
A-level (85-100% of the relevant features’ points):
The cloth moves with smooth, natural, real-time motion. The simulation scenario is
clearly different than the basic cloth, and showcases interesting motion and new
types of simulations and interactions.
B-level (70-85% of the relevant features’ points):
The cloth looks like it is moving in “slow-mode” and/or this new simulation doesn’t
show case substantially different motion types than the first cloth simulation. The
added elements effect the motion of the cloth, but there may be some unnaturalness
to the motion, or the effect of the new simulation elements are somewhat subtle.
C-level: (55-70% of the relevant features’ points):
The motion of the cloth is different from the first cloth simulation, but not in a way
which showcases the new features very clearly (e.g., in one simulation the cloth falls
horizontally, the other vertically). Or the features added look somewhat unnatural
in the way the interact with the cloth.


