package org.egov.eis.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        int noOfLanes=0;
        List<Integer> laneWidthList = new ArrayList<Integer>();
        boolean firstLine = true;
        boolean secondLine = true;
        while(stdin.hasNextLine()) {
                if(firstLine){
                    noOfLanes = stdin.nextInt();
                    stdin.nextInt();
                    firstLine = false;
                }
                else
                    {
                    if(secondLine){
                        for(int i=0;i<noOfLanes;i++) {
                            laneWidthList.add(stdin.nextInt());
                        }
                        secondLine = false;
                    }    
                    else{
                        int entryLane = stdin.nextInt();
                        int exitLane  = stdin.nextInt();
                        List<Integer> subList = new ArrayList<Integer>();
                        for(int i=entryLane;i<=exitLane;i++){
                            subList.add(laneWidthList.get(i));
                        }
                        System.out.println(Collections.min(subList));
                    }
                }
            }
    }
}
    
   /* public static void main(String []argh)
    {
        Scanner in = new Scanner(System.in);
        while(in.hasNext())
        {
            String IP = in.next();
            System.out.println(IP.matches("([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])"));
        }

    }*/
   /* public static void main(String[] args) {
         Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. 
        Scanner stdin = new Scanner(System.in);
        boolean skipFirstElement = false;
        int a,b,n;
        int sum=0;
        while(stdin.hasNextLine()) {
            if(!skipFirstElement) {
                stdin.nextLine();
                skipFirstElement=true;
            }
            else
                {
                    a= stdin.nextInt();
                    b= stdin.nextInt();
                    n= stdin.nextInt();
                    for(int i=0;i<n;i++) {
                        sum = (int) (sum+ (a+(Math.pow(2, i))*b));
                        System.out.print(sum+" ");
                    }
                    System.out.println();
            }
            
        }
    }*/
