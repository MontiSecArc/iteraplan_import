# About
The Itestra Importer plugin allows the user to import architecture information from an existing system into a MontiSecArc architecture description. This allows the security export to run flaw analysis on the architectures with the toolchain created for the IntelliJ IDEA platform.

**Downloads**

[]() &#8226; []()


# Contents
- [Quickstart](https://git.rwth-aachen.de/ma_buning/itestra_importer/edit/master/README.md#quickstart)
- [Standalone Transformation](https://git.rwth-aachen.de/ma_buning/itestra_importer/edit/master/README.md#standalone_transformation)

# Quickstart 
1. Check-Out project:

    `git clone https://git.rwth-aachen.de/ma_buning/itestra_importer.git --remote --recursive`
2. Import project into IntelliJ. Instructions can be found [here](https://www.jetbrains.com/help/idea/2016.3/importing-project-from-gradle-model.html).
3. Run an IDEA instance with the MSA language plugins pre-installed:
    1. Run/Debug `runIdea` from the gradle task list:
   ![Bildschirmfoto_2017-01-10_um_18.28.47](/uploads/80e487891bf88109bea2797901578747/Bildschirmfoto_2017-01-10_um_18.28.47.png)
4. Import new architecture from json file by selecting File > New > Import from Itestra Export 

# Standalone Transformation
To test the transformation into MontiSecArc files, no integration into IntelliJ IDEA necessary. The submodule *IteraplanImporter* has a test-case where the imported file can be selected and therefore, the import can be tested. Furthermore, the class *Main* can act as a single jar, providing everything to kick-off an import from the command-line.