# FRC-Test

Simple Emulation package for FIRST Robotics Java code running on a roboRIO

### Adding it to your project

1. Extract the FRC-Test zip file into the root of your robot code project

2. Add the followinc code at the bottom of your build.xml, right before `</project>`:
```
  <!-- +++++++++++++++++++++++++++++++++++++   FRC-Test   +++++++++++++++++++++++++++++++++++++ -->
  <import file="frctest.xml"/>
```

3. Create a new Ant Run Configuration by right-clicking build.xml in the project explorer and choosing Run As > Ant Build...
Select the `runemulator` target in the targets tab.
Then, run the configuratin, and the emulator will search for all classes extending IterativeRobot and ask which one you want to run. 
