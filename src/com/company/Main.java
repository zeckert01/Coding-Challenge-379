package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static String taxFileName = "F:\\OneDrive\\Coding Challenges\\Challenge379\\src\\com\\company\\TaxBrackets.txt";
    static double maxIncome = 100000000.0;
    public static void main(String[] args) {
        ArrayList<double[]> taxBrackets = readBrackets(taxFileName);
        System.out.println("$12,000: " + computeTax(taxBrackets,12000));
        System.out.println("$56,789: " + computeTax(taxBrackets,56789));
        System.out.println("$1,234,567: " + computeTax(taxBrackets,1234567));
        System.out.println("$256,250: " + computeTax(taxBrackets,256250));

    }
    public static ArrayList<double[]> readBrackets(String fileName) {
        /*
        Reads tax bracket information from a csv file and outputs a list of tax bracket arrays. The input file is of the form:
        cap1, tax1
        cap2, tax2
        ...
        -, taxTop
        */
        File file = new File(fileName);
        String line = "";
        ArrayList<double[]> brackets = new ArrayList<double[]>(1);
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                String[] bracketStr = line.split(",");
                if (!bracketStr[0].equals("-")) {
                    brackets.add(Arrays.stream(bracketStr).mapToDouble(Double::parseDouble).toArray());
                } else {
                    double[] upperTax = new double[] {maxIncome , Double.parseDouble(bracketStr[1])};
                    brackets.add(upperTax);
                }
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return brackets;

    }
    public static String ArrayListToString(ArrayList<double[]> list) {
        /*
        Print ArrayList of double arrays
         */
        String output = "";
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                output += list.get(i)[j];
                output += "\t";
            }
            output += "\n";
        }
        return output;
    }
    public static int computeTaxAttempt1(ArrayList<double[]> taxBrackets, double income) {
        int numBrackets = taxBrackets.size();
        double tempIncome = income;
        double totalTax = 0;
        double[] taxableIncome = new double[numBrackets];
        for (int i = 0; i < numBrackets; i++){
            if (i != numBrackets - 1) {
                if (income > taxBrackets.get(i)[0]) {
                    taxableIncome[i] = taxBrackets.get(i)[0];
                    if (i != 0) {
                        taxableIncome[i] -= taxBrackets.get(i - 1)[0];
                    }
                    tempIncome = income - taxBrackets.get(i)[0];
                } else {
                    if (tempIncome < 0) {
                        taxableIncome[i] = 0;
                    } else {
                        taxableIncome[i] = tempIncome;
                        tempIncome = 0;
                    }
                }
            } else {
                if (tempIncome < 0) {
                    taxableIncome[i] = 0;
                } else {
                    taxableIncome[i] = tempIncome;
                }
            }
        }
        for (int i = 0; i < numBrackets; i++) {
            totalTax += taxableIncome[i] * taxBrackets.get(i)[taxBrackets.get(i).length - 1];
        }
        return (int) totalTax;
    }
    public static int computeTax(ArrayList<double[]> taxBrackets, double income) {
        double tax = 0;
        for (int i = 0; i < taxBrackets.size(); i++) {
            if (i == 0) {
                tax += Math.min(income, taxBrackets.get(i)[0]) * taxBrackets.get(i)[1];
            } else {
                tax += Math.min(Math.max(income - taxBrackets.get(i - 1)[0], 0), taxBrackets.get(i)[0] - taxBrackets.get(i - 1)[0]) * taxBrackets.get(i)[1];
            }
        }
        return (int)tax;
    }
}
