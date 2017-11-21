/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.VmList;

import java.util.Random;

/**
 * VmAllocationPolicySimple is an VmAllocationPolicy that chooses, as the host for a VM, the host
 * with less PEs in use. It is therefore a Worst Fit policy, allocating VMs into the 
 * host with most available PE.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class VmAllocationPolicyCustomized{
	private static boolean flag=false;
	private static ArrayList<Integer> maptemp=new ArrayList<Integer>();
	private static ArrayList<Vm> migrationVmList=new ArrayList<Vm>();
	private static ArrayList<Host> hotspotList= new ArrayList<Host>();
	private static ArrayList<Host> nothotspotList= new ArrayList<Host>();
	List<Vm> tempvmlist = new ArrayList<Vm>();
	List<Long> totalutilization=new ArrayList<Long>();
	/*private static double max_util_cpu=0.0;
	private static double max_util_mem=0.0;
	private static double max_util_bw=0.0;
	private static double min_util_cpu=0.0;
	private static double min_util_mem=0.0;
	private static double min_util_bw=0.0;*/
	public VmAllocationPolicyCustomized(){
		//flag=true;
		//maptemp=null;
	}
	
	public ArrayList<Integer> allocationForMigration(List<Host> hostList, List<Vm> vmList){
		
		//HashMap<Integer,Integer> maptemp=new HashMap<Integer,Integer>();
		if (flag==false)
		{
			
			flag=true;
			System.out.println("flag value is " + flag);
			
			
			for (Host host : hostList) {
				List<Pe> peList=host.getPeList();
				int pemips=0;
				System.out.println("pelist size: "+peList.size());
				for(int i=0;i<peList.size();i++)
				{
					pemips=pemips+peList.get(i).getMips();
				}
				System.out.println("1: "+pemips);
				System.out.println("2: "+host.getMaxAvailableMips());
				System.out.println("3: "+host.getTotalMips());
				double max_util_cpu=host.getTotalMips()*0.8;
				double max_util_mem=host.getRam()*0.8;
				double max_util_bw=host.getBw()*0.8;
				double min_util_cpu=host.getTotalMips()*0.2;
				double min_util_mem=host.getRam()*0.2;
				double min_util_bw=host.getBw()*0.2;
				double mips=0.0;
				double ram=0;
				double bw=(long) 0.0;
			for (Vm vm : vmList) {
				
				if (host.getId()==vm.getHost().getId()) {
					mips=mips+vm.getTotalUtilizationOfCpuMips(CloudSim.clock());
					//System.out.println("mips of vm 1: "+vm.getCurrentAllocatedMips());
					System.out.println("mips of vm 2: "+vm.getTotalUtilizationOfCpuMips(CloudSim.clock()));
					ram=ram+vm.getCurrentAllocatedRam();
					bw=bw+vm.getCurrentAllocatedBw();
				}
			}	
			if (mips>max_util_cpu || ram>max_util_mem || bw>max_util_bw) {
				System.out.println("Maxed out");
				
				if (mips>max_util_cpu) {
					System.out.println("MIPS maxed");
				}
				System.out.println("host id of hotspot identified host is: "+host.getId());
				hotspotList.add(host);
			}
			else if (mips<min_util_cpu || ram<min_util_mem || bw<min_util_bw) {
				System.out.println("mips: "+mips);
				System.out.println("mips max: "+max_util_cpu);
				if (bw<min_util_bw) {
					System.out.println("MIPS mined out");
				}
				hotspotList.add(host);
			}
			else {
				nothotspotList.add(host);
			}
				
			}
			
			System.out.println("not hotspot list size: "+nothotspotList.size());
			System.out.println("hotspot list size: "+hotspotList.size());
			
			if (!(totalutilization.isEmpty())) {
				for (int d=totalutilization.size()-1;d>=0;d--) {
					totalutilization.remove(d);
				}
			}
			System.out.println("Unsorted hotspot host list");
			for (int v=0;v<hotspotList.size();v++) {
				System.out.println(hotspotList.get(v).getId());
			}
			for (Host host : hotspotList) {
				long total= (long)0.0;
				List<Vm> vmlistofhost=host.getVmList();
				for (Vm vm:vmlistofhost) {
					total=(long) (total+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
				}
				totalutilization.add(total);	
			}
			quicksort1(0,totalutilization.size()-1);
			
			System.out.println("Sorted hotspot host list");
			for (int v=0;v<hotspotList.size();v++) {
				System.out.println(hotspotList.get(v).getId());
			}
			
			/*for (Host host : hotspotList) {
				long total= (long)0.0;
				List<Vm> vmlistofhost=host.getVmList();
				for (Vm vm:vmlistofhost) {
					total=(long) (total+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
				}
				long total2=(long) 0.0;
				for (Host temphost : hotspotList) {
					List<Vm> tempvmlistofhost=temphost.getVmList();
					if(host.getId()!=temphost.getId()) {
						for (Vm vm:tempvmlistofhost) {
							total2=(long) (total2+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
						}
						if(total2>total) {
							System.out.println("Sorting.........................");
							//Host temp=host;
							int index1=hotspotList.indexOf(host);
							int index2=hotspotList.indexOf(temphost);
							hotspotList.set(index1, temphost);
							hotspotList.set(index2, host);
						}
					}
				}	
			}*/
			

			
			
			/*System.out.println("Unsorted non hotspot host list");
			for (int v=0;v<nothotspotList.size();v++) {
				System.out.println(nothotspotList.get(v).getId());
			}
			for (Host host : nothotspotList) {
				long total= (long)0.0;
				List<Vm> vmlistofhost=host.getVmList();
				for (Vm vm:vmlistofhost) {
					total=(long) (total+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
				}
				long total2=(long) 0.0;
				for (Host temphost : nothotspotList) {
					List<Vm> tempvmlistofhost=temphost.getVmList();
					if(host.getId()!=temphost.getId()) {
						for (Vm vm:tempvmlistofhost) {
							total2=(long) (total+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
						}
						if(total2<total) {
							//Host temp=host;
							int index1=nothotspotList.indexOf(host);
							int index2=nothotspotList.indexOf(temphost);
							nothotspotList.set(index1, temphost);
							nothotspotList.set(index2, host);
						}
					}
				}	
			}
			System.out.println("Sorted non hotspot host list");
			for (int v=0;v<nothotspotList.size();v++) {
				System.out.println(nothotspotList.get(v).getId());
			}*/
			
			/*if (!(totalutilization.isEmpty())) {
			for (int d=totalutilization.size()-1;d>=0;d--) {
				totalutilization.remove(d);
			}
		    }
			System.out.println("Unsorted non hotspot host list");
			for (int v=0;v<nothotspotList.size();v++) {
				System.out.println(nothotspotList.get(v).getId());
			}
			for (Host host : nothotspotList) {
				long total= (long)0.0;
				List<Vm> vmlistofhost=host.getVmList();
				for (Vm vm:vmlistofhost) {
					total=(long) (total+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
				}
				totalutilization.add(total);	
			}
			quicksort2(0,totalutilization.size()-1);
			
			System.out.println("Sorted non hotspot host list");
			for (int v=0;v<nothotspotList.size();v++) {
				System.out.println(nothotspotList.get(v).getId());
		    }*/
			
			for (Host host : hotspotList) {
				System.out.println("Hotspots found");
				System.out.println("Host id of hotspot host is: "+host.getId());
				//List<Vm> tempvmlist = host.getVmList();
				//List<Long> totalutilization=new ArrayList<Long>();
				tempvmlist=host.getVmList();
				if (!(totalutilization.isEmpty())) {
					for (int d=totalutilization.size()-1;d>=0;d--) {
						totalutilization.remove(d);
					}
				}
				for (Vm vm:tempvmlist) {
					long total= (long)0.0;
					total=(long) (total+vm.getTotalUtilizationOfCpuMips(CloudSim.clock())+vm.getCurrentAllocatedRam()+vm.getCurrentAllocatedBw());
					totalutilization.add(total);
				}
				long start=System.currentTimeMillis();
				for (int i=0;i<tempvmlist.size();i++) {
					System.out.println(tempvmlist.get(i).getId());
				}
				//quicksort3(0, totalutilization.size()-1);
				int vm_index=quickselect(1);
				/*for (int i=0;i<totalutilization.size();i++) {
					for (int j=0;j<totalutilization.size();j++) {
						if(totalutilization.get(i)<totalutilization.get(j)) {
							Vm vm1=tempvmlist.get(i);
							Vm vm2=tempvmlist.get(j);
							tempvmlist.set(i, vm2);
							tempvmlist.set(j, vm1);
							long temp1=totalutilization.get(i);
							long temp2=totalutilization.get(j);
							totalutilization.set(i,temp2);
							totalutilization.set(j,temp1);
						}
					}
				}*/
				long stop=System.currentTimeMillis();
				System.out.println("Result of VM sorting is:----------------------------------------");
				for (int i=0;i<tempvmlist.size();i++) {
					System.out.println(tempvmlist.get(i).getId());
				}
				System.out.println("Result of VM sorting over:----------------------------------------");
				System.out.println("Sort time for VM is: "+(stop-start));
				
				//Vm vm=tempvmlist.get(0);
				Vm vm=tempvmlist.get(vm_index);
				int host_id=random_fit(vm);
				/*double targetmips=vm.getTotalUtilizationOfCpuMips(CloudSim.clock());;
				int targetram=vm.getCurrentAllocatedRam();
				long targetbw=vm.getCurrentAllocatedBw();
				
				List<Double> totalutilizationdifference=new ArrayList<Double>();
				
				for (Host targethost:nothotspotList) {
					double targethost_max_cpu=targethost.getTotalMips()*0.8;
					double targethost_max_ram=targethost.getRam()*0.8;
					double targethost_max_bw=targethost.getBw()*0.8;
					double targethost_cpu=0.0;
					double targethost_ram=0.0;
					double targethost_bw=0.0;
					List<Vm> tagethostvmlist = targethost.getVmList();
					for (Vm targethostvm:tagethostvmlist) {
						targethost_cpu=targethost_cpu+targethostvm.getTotalUtilizationOfCpuMips(CloudSim.clock());
						targethost_ram=targethost_ram+targethostvm.getCurrentAllocatedRam();
						targethost_bw=targethost_bw+targethostvm.getCurrentAllocatedBw();
					}
				
					if ((targethost_cpu+targetmips)<targethost_max_cpu) {
						if ((targethost_ram+targetram)<targethost_max_ram) {
							if ((targethost_bw+targetbw)<targethost_max_bw) {
								double utilizationdifference=(targethost_cpu-targetmips)+(targethost_ram-targetram)+(targethost_bw-targetbw);
								totalutilizationdifference.add(utilizationdifference);
							}
						}
					}
				}*/
				
				double minimum_difference=9000000000000000000000000.0;
				int targethostindex=host_id;
				//int targethostindex=-1;
				/*for (int i=0;i<totalutilizationdifference.size();i++) {
					System.out.println("total util diff is: "+totalutilizationdifference.get(i));
					if (totalutilizationdifference.get(i)<minimum_difference) {
						minimum_difference=totalutilizationdifference.get(i);
						targethostindex=i;
						System.out.println("Success!");
					}
				}*/
				
				if (targethostindex==-1) {
					System.out.println("System out of control!!!");
				}
				maptemp.add(vm.getId());
				maptemp.add(nothotspotList.get(targethostindex).getId());
				migrationVmList.add(vm);
			}
			
			/*for (Host host : nothotspotList) {
				
				List<Vm> vmlistofhost=host.getVmList();
				for (Vm vm:vmlistofhost) {
					maptemp.put(vm.getId(),host.getId());
				}
				
			}*/
			
			/*for (int i=0;i<vmList.size();i++){
				
				maptemp.put(i,1);
			}*/
			
			//return maptemp;
		}
		else if(flag==true)
		{
			System.out.println("No change");
			//return maptemp;
		}
		return maptemp;
		//return migrationVmList;
		//return null;
		
	}
	
 public int partitionFunc1(int left,int right,long pivot) {
	 int leftPointer=left;
	 int rightPointer=right-1;
	 
	 while (true) {
		 while ((totalutilization.get(++leftPointer)>pivot)){
			 
		 }
		 while (rightPointer>0 && (totalutilization.get(--rightPointer)<pivot)){
			 
		 }
		 if (leftPointer >= rightPointer) {
			 break;
		 }
		 else {
			 Host host1=hotspotList.get(leftPointer);
			 Host host2=hotspotList.get(rightPointer);
			 hotspotList.set(leftPointer, host2);
			 hotspotList.set(rightPointer, host1);
			 long temp1=totalutilization.get(leftPointer);
			 long temp2=totalutilization.get(rightPointer);
			 totalutilization.set(leftPointer,temp2);
			 totalutilization.set(rightPointer,temp1);
		 }
	 }
	 
	 Host host1=hotspotList.get(leftPointer);
	 Host host2=hotspotList.get(rightPointer);
	 hotspotList.set(leftPointer, host2);
	 hotspotList.set(rightPointer, host1);
	 long temp1=totalutilization.get(leftPointer);
	 long temp2=totalutilization.get(right);
	 totalutilization.set(leftPointer,temp2);
	 totalutilization.set(right,temp1);
	 
	 return leftPointer;
	 
 }
 
 public int partitionFunc2(int left,int right,long pivot) {
	 int leftPointer=left;
	 int rightPointer=right-1;
	 
	 while (true) {
		 while ((totalutilization.get(++leftPointer)<pivot)){
			 
		 }
		 while (rightPointer>0 && (totalutilization.get(--rightPointer)>pivot)){
			 
		 }
		 if (leftPointer >= rightPointer) {
			 break;
		 }
		 else {
			 Host host1=nothotspotList.get(leftPointer);
			 Host host2=nothotspotList.get(rightPointer);
			 nothotspotList.set(leftPointer, host2);
			 nothotspotList.set(rightPointer, host1);
			 long temp1=totalutilization.get(leftPointer);
			 long temp2=totalutilization.get(rightPointer);
			 totalutilization.set(leftPointer,temp2);
			 totalutilization.set(rightPointer,temp1);
		 }
	 }
	 
	 Host host1=nothotspotList.get(leftPointer);
	 Host host2=nothotspotList.get(rightPointer);
	 nothotspotList.set(leftPointer, host2);
	 nothotspotList.set(rightPointer, host1);
	 long temp1=totalutilization.get(leftPointer);
	 long temp2=totalutilization.get(right);
	 totalutilization.set(leftPointer,temp2);
	 totalutilization.set(right,temp1);
	 
	 return leftPointer;
	 
 }
 
 public int partitionFunc3(int left,int right,long pivot) {
	 int leftPointer=left;
	 int rightPointer=right-1;
	 
	 while (true) {
		 while ((totalutilization.get(++leftPointer)>pivot)){
			 
		 }
		 while (rightPointer>0 && (totalutilization.get(--rightPointer)<pivot)){
			 
		 }
		 if (leftPointer >= rightPointer) {
			 break;
		 }
		 else {
			 Vm vm1=tempvmlist.get(leftPointer);
			 Vm vm2=tempvmlist.get(rightPointer);
			 tempvmlist.set(leftPointer, vm2);
			 tempvmlist.set(rightPointer, vm1);
			 long temp1=totalutilization.get(leftPointer);
			 long temp2=totalutilization.get(rightPointer);
			 totalutilization.set(leftPointer,temp2);
			 totalutilization.set(rightPointer,temp1);
		 }
	 }
	 
	 Vm vm1=tempvmlist.get(leftPointer);
	 Vm vm2=tempvmlist.get(right);
	 tempvmlist.set(leftPointer, vm2);
	 tempvmlist.set(right, vm1);
	 long temp1=totalutilization.get(leftPointer);
	 long temp2=totalutilization.get(right);
	 totalutilization.set(leftPointer,temp2);
	 totalutilization.set(right,temp1);
	 
	 return leftPointer;
	 
 }
 
 public void quicksort1(int left, int right) {
	 if (right-left<=0) {
		 return;
	 }
	 else {
		 long pivot=totalutilization.get(right);
		 int partition=partitionFunc1(left,right,pivot);
		 quicksort1(left,partition-1);
		 quicksort1(partition+1,right);
		 
	 }
 }
 
 public void quicksort2(int left, int right) {
	 if (right-left<=0) {
		 return;
	 }
	 else {
		 long pivot=totalutilization.get(right);
		 int partition=partitionFunc2(left,right,pivot);
		 quicksort2(left,partition-1);
		 quicksort2(partition+1,right);
		 
	 }
 }
 
 public void quicksort3(int left, int right) {
	 if (right-left<=0) {
		 return;
	 }
	 else {
		 long pivot=totalutilization.get(right);
		 int partition=partitionFunc3(left,right,pivot);
		 quicksort3(left,partition-1);
		 quicksort3(partition+1,right);
		 
	 }
 }
 
 public int partition_quickselect(int flag,int start,int end) {
	 int pivot=start;
	 if (flag==0){
		 Random rand=new Random();
		 pivot=rand.nextInt(end+1);
	 }
	 while (start<= end) {
		 while (start<=end && totalutilization.get(start)<=totalutilization.get(pivot)) {
			 start++;
		 }
		 while (start<=end && totalutilization.get(end)>totalutilization.get(pivot)) {
			 end--;
		 }
		 if (start>end) {break;}
		 Vm vm1=tempvmlist.get(start);
		 Vm vm2=tempvmlist.get(end);
		 tempvmlist.set(start, vm2);
		 tempvmlist.set(end, vm1);
		 long temp1=totalutilization.get(start);
		 long temp2=totalutilization.get(end);
		 totalutilization.set(start,temp2);
		 totalutilization.set(end,temp1);
		 
	 }
	 Vm vm1=tempvmlist.get(pivot);
	 Vm vm2=tempvmlist.get(end);
	 tempvmlist.set(pivot, vm2);
	 tempvmlist.set(end, vm1);
	 long temp1=totalutilization.get(pivot);
	 long temp2=totalutilization.get(end);
	 totalutilization.set(pivot,temp2);
	 totalutilization.set(end,temp1);
	 return end;
 }
 
 public int quickselect(int k) {
	 int start=0, end=totalutilization.size()-1, index=totalutilization.size()-k;
	 int flag=0;
	 while(start<end) {
		 int pivot=partition_quickselect(flag, start, end);
		 flag=1;
		 if(pivot<index) {
			 start=pivot+1;
		 }
		 else if (pivot>index) {
			 end=pivot-1;
		 }
		 else {
			 return pivot;
		 }
	 }
	 return start;
 }
 
public int random_fit(Vm vm) {
	int host_index=-1;
	double targetmips=vm.getTotalUtilizationOfCpuMips(CloudSim.clock());;
	int targetram=vm.getCurrentAllocatedRam();
	long targetbw=vm.getCurrentAllocatedBw();
	ArrayList<Integer> myInts=new ArrayList<Integer>();
	for (int k=0;k<nothotspotList.size();k++) {
		myInts.add(k);
	}
	HashMap<Integer,Host> tempnothotspotList= new HashMap<Integer,Host>();
	Random rand=new Random();
	int index=0;
	while (index<nothotspotList.size()) {
		Integer i=myInts.get(rand.nextInt(myInts.size()));
	
		tempnothotspotList.put(i, nothotspotList.get(index));
		index++;
		myInts.remove(i);	
		}
	
	for (int j=0;j<tempnothotspotList.size();j++) {
		Host targethost=tempnothotspotList.get(j);
		double targethost_max_cpu=targethost.getTotalMips()*0.8;
		double targethost_max_ram=targethost.getRam()*0.8;
		double targethost_max_bw=targethost.getBw()*0.8;
		double targethost_cpu=0.0;
		double targethost_ram=0.0;
		double targethost_bw=0.0;
		List<Vm> tagethostvmlist = targethost.getVmList();
		for (Vm targethostvm:tagethostvmlist) {
			targethost_cpu=targethost_cpu+targethostvm.getTotalUtilizationOfCpuMips(CloudSim.clock());
			targethost_ram=targethost_ram+targethostvm.getCurrentAllocatedRam();
			targethost_bw=targethost_bw+targethostvm.getCurrentAllocatedBw();
		}
	
		if ((targethost_cpu+targetmips)<targethost_max_cpu) {
			if ((targethost_ram+targetram)<targethost_max_ram) {
				if ((targethost_bw+targetbw)<targethost_max_bw) {
					host_index=nothotspotList.indexOf(targethost);
					break;
				}
			}
		}
		
	}
	
	return host_index;
	
}
}

