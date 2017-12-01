# cloudsim_randomized_vm_migration_algorithm

This is the repository having code for the Modified Best Fit Decreasing algorithm of VM migration.
Also, this code has the randomized algorithm for VM Migration. 

How to run the code:

1. Download the source code for cloudsim from cloudsim website.
2. In the cloudsim code base, you will find the 3 files, that are also avaialable in this repo.
3. Make sure that you are looking at exactly the same files in the cloudsim as the ones that are posted here in the repo.
4. Pay special attenstion to the import statements at top of the files in this repo. These statements will give you the package and library name under which these files are located in cloudsim.
5. Now, locate and replace these 3 files with the files in this repo.
6. Go  to VmAllocationPolicyCustomized.java file. In this file, there is a variable called "choice" in the allocationForMigration function.
7. If you want to run MBFD algorithm, set choice = 1, else to run randomized algorithm set choice = 2. Dont set for any other value, but these two values, otherwise your code will not run.
8. After setting the value of variable "choice" in allocationForMigration function of VmAllocationPolicyCustomized.java class, save the file VmAllocationPolicyCustomized.java.
9. Finally go to CloudSimExample3.java file. Run this file as Java application. You will see the results in the console. 

NOTE : YOU WILL HAVE TO SCROLL UP ON CONSOLE TO SEE LOG STATEMENTS SHOWING OUTPUT FOR MIGRATION OF THE VM.

Thanks!
