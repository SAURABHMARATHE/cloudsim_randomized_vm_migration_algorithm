# cloudsim_randomized_vm_migration_algorithm

This is the repository having code for the Modified Best Fit Decreasing algorithm of VM migration.
Also, this code has the randomized algorithm for VM Migration. Quickselect with randomized pivot is used for selecting VM with highest utilization for each host. Random fit bin packing is used for selecting target host for each migrating VM.

How to run the code:

1. Download all the 3 files.
2. Replace file ClousimExample3.java from org.cloudbus.cloudsim.examples package in the Cloudsim code
3. Replace file Datacenter.java and VmAllocationPolicyCustomized.java from org.cloudbus.cloudsim package in CloudSim code
4. Run the CloudSimExample3.java file.

By default the current code is running the randomized algorithms of Quickselect and Random Fit bin packing.
For MBFD algorithm part to run instead of randomized algorithm:
1. Uncomment line 200 to line 222
2. Uncomment line 244
3. Comment line 245
4. Uncomment line 268
5. Comment line 269
6. Comment line 270
7. Uncomment line 271 to line 299
8. Comment line 302
9. Uncomment line 303 to line 311

Now run the CLoudsimExample3.java file again, and you will see results of MBFD algorithm based VM migration.

Important note: Cloudsim is a simulation software. Hence, running the same algorithm again yields different runtime everytime. Hence, comparison of MBFD and Randomized algorithms (Quickselect and Random Fit) is possible through paper based time complexity anaysis.
Also, it is possible by running both the programmes and seeing if Randomized algorithm finishes with runtime lower than the MBFD at least once. Also, it is important to note, that Randomized algorithm will produce worst case solution sometimes, becasue of inherent randomness (exampe: random choice of pivot in case of Quickselect resulting in worst choice of pivot for cerrtain run)

Thank You!
