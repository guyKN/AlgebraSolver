package com.knstudios.guyknaan.algebranew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private int regexSolve(String regX ,String str){
        Pattern pattern = Pattern.compile(regX);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.start();
        }else{
            return -1;
        }
    }

    private String floatToString(double f){
        int Fint = (int) f;
        if(f == Fint){
            ////Log.d("D1","return int");
            return "" + Fint;
        }else{
            ////Log.d("D1","return double");
            //return "" + Math.round(f/1000)*1000;
            return "" + Math.round(f * 10000.0) / 10000.0;
            //return ""+f;
        }
    }

    private String replace(String original, char find, String replace){

        for (int i=0; i<original.length(); i++)
        {
            if(original.charAt(i) == find){
                original = original.substring(0,i) + replace + original.substring(i+1);
                i+= replace.length()-1;
            }
        }
        return original;
    }

    private ArrayList<ArrayList<ArrayList<Double>>> simplifyMult(ArrayList<ArrayList<ArrayList<String>>> equationSplitByTimes){

        ArrayList<ArrayList<ArrayList<Double>>> equationExpForm = new ArrayList(Arrays.asList(new ArrayList(), new ArrayList(), new ArrayList()));//product, numX, Division



        for(int side = 0; side<equationSplitByTimes.size();side++){
            equationExpForm.add(new ArrayList());

            for(int term = 0; term<equationSplitByTimes.get(side).size();term++){

                equationExpForm.get(side).add(new ArrayList());

                Double product = Double.valueOf(1);
                Double division = Double.valueOf(1);
                int numX = 0;

                for(int multLocation = 0; multLocation<equationSplitByTimes.get(side).get(term).size();multLocation++) {
                    String curentStr = equationSplitByTimes.get(side).get(term).get(multLocation);
                    if(!"".equals(curentStr)) {
                        if (curentStr.charAt(0) == "x".charAt(0)) {
                            if (!(curentStr.length() == 1)) {
                                if (curentStr.charAt(1) == "^".charAt(0)) {
                                    if (curentStr.substring(2) != "") {
                                        numX += Integer.parseInt(curentStr.substring(2, curentStr.length()));
                                    }
                                } else {
                                    numX++;
                                }
                            } else {
                                numX++;
                            }
                        } else if (curentStr.charAt(0) == "/".charAt(0)) {
                            if (curentStr.charAt(1) == "x".charAt(0)) {
                                if (!(curentStr.length() == 2)) {
                                    if (curentStr.charAt(2) == "^".charAt(0)) {
                                        if (curentStr.substring(3) != "") {
                                            numX += Integer.parseInt(curentStr.substring(3, curentStr.length()));
                                        }
                                    } else {
                                        numX--;
                                    }
                                } else {
                                    numX--;
                                }
                            } else {
                                if (curentStr.substring(1) != "") {
                                    division *= Double.parseDouble(curentStr.substring(1));
                                }
                            }

                        } else {
                            if (curentStr != "") {
                                product *= Double.parseDouble(curentStr);
                            }
                        }
                    }
                }
                //Log.d("DDDDD","numX " + numX + " product " + product + " division " + division);
                equationExpForm.get(side).get(term).add(product);
                equationExpForm.get(side).get(term).add( (double)numX );
                equationExpForm.get(side).get(term).add(division);
            }

        }

        while(equationExpForm.remove(new ArrayList())){}
        ////Log.d("DDDDD", "final exponental form: "+ equationExpForm);

        return equationExpForm;
    }

    private String expToString(ArrayList<ArrayList<ArrayList<Double>>> equationExpForm){
        StringBuilder returnEq2 = new StringBuilder();
        int ind1 = 0;
        for (ArrayList<ArrayList<Double>> side : equationExpForm) {
            int ind2 = 0;
            for (ArrayList<Double> term : side) {
                if(term.get(0) != 1 || term.get(1) == 0){
                    returnEq2.append(floatToString(term.get(0)));
                }

                if(term.get(1) != 0){
                    returnEq2.append("x");
                    if(term.get(1) != 1) {
                        returnEq2.append("^");
                        returnEq2.append(floatToString(term.get(1)));
                    }
                }

                if(term.get(2) != 1){
                    returnEq2.append("/");
                    returnEq2.append(floatToString(term.get(2)));
                }
                ind2++;
                ////Log.d("D6", "index " + ind1 );
                //
                ////Log.d("D6", "max " + side.get(ind1).size() );
                if(ind2 != side.size()) {
                    returnEq2.append("+");
                }

            }
            ind1++;


            if( Arrays.asList(returnEq2.toString().split("=")).contains("") ){
                returnEq2.append("0");
            }
            ////Log.d("D6", "doing equal");
            returnEq2.append("=");

        }

        return returnEq2.toString().substring(0,returnEq2.length()-1);
    }

    private ArrayList<ArrayList<ArrayList<String>>> startOps(String equationInp){

        String equationWithTimes = equationInp;

        int index = regexSolve("[0-9]+\\.*[0-9]*x", equationWithTimes);
        ////Log.d("equationWithTimes", "index:" + index);
        while(index != -1){
            boolean found = false;
            while(!found){
                if (equationWithTimes.charAt(index) == "x".charAt(0)){
                    found = true;
                    equationWithTimes = equationWithTimes.substring(0,index) + "*" + equationWithTimes.substring(index, equationWithTimes.length());
                }else{
                    index++;
                }
            }
            index = regexSolve("[0-9]+\\.*[0-9]*x", equationWithTimes);
        }


        ////Log.d("equationWithTimes", equationWithTimes);

        List<String> equationSplit = Arrays.asList(equationWithTimes.split("="));



        if (equationSplit.size() != 2){
            return new ArrayList(Arrays.asList("error", "Error:you must have exacly 1 equal sign in your equation"));
        }

        ArrayList<String> equation = new ArrayList();

        for (int i=0; i<equationSplit.size(); i++) {

            ////Log.d("DDD","equation replaced -: " + replace(equationSplit.get(i), "-".charAt(0), "+-"));
            equation.add(replace(equationSplit.get(i), "-".charAt(0), "+-"));

            ////Log.d("DDD","equation replaced /: " + replace(equationSplit.get(i), "-".charAt(0), "+-"));
            equation.set(i, replace(equation.get(i), "/".charAt(0), "*/"));
        }

        ArrayList<ArrayList<String>> equationSplitByPlus = new ArrayList(Arrays.asList(new ArrayList(), new ArrayList()));

        for (int i=0; i<equation.size(); i++) {

            ArrayList<String> splitByPlus = new ArrayList<>(Arrays.asList(equation.get(i).split("\\+")));

            equationSplitByPlus.set(i, splitByPlus);
            ////Log.d("DDD","doing spliting by +: " + splitByPlus );

        }

        ////Log.d("DDD", "final split by +: "+ equationSplitByPlus);

        ArrayList<ArrayList<ArrayList<String>>> equationSplitByTimes = new ArrayList(Arrays.asList(new ArrayList(), new ArrayList()));

        for (int i=0; i<equationSplitByPlus.size(); i++) {
            for (int j=0; j<equationSplitByPlus.get(i).size(); j++) {
                ArrayList<String> splitByTimes = new ArrayList<>(Arrays.asList(equationSplitByPlus.get(i).get(j).split("\\*")));
                equationSplitByTimes.get(i).add(j, splitByTimes);
                ////Log.d("DDDD", "doing spliting by *: " + splitByTimes + ", i =" + i + ",j=" + j);
            }
        }

        ////Log.d("DDD", "final split by *: "+ equationSplitByTimes);
        return equationSplitByTimes;
    }

    private ArrayList<ArrayList<ArrayList<Double>>> rmvDivision(ArrayList<ArrayList<ArrayList<Double>>> equationExpForm){
        Double product = 1.0;
        for(ArrayList<ArrayList<Double>> side:equationExpForm){
            for(ArrayList<Double> term:side){
                product *= term.get(2);
                term.set(0, term.get(0)/term.get(2));
                term.set(2,1.0);
            }
        }

        for(ArrayList<ArrayList<Double>> side:equationExpForm){
            for(ArrayList<Double> term:side){
                term.set(0,term.get(0)*product);
            }
        }

        return equationExpForm;
    }

    private ArrayList<ArrayList<Double>> combineTerms(ArrayList<ArrayList<ArrayList<Double>>> equationExpForm, int maxExp){



        ArrayList<ArrayList<Double>> polynomial = new ArrayList();



        polynomial.add(new ArrayList<Double>());
        polynomial.add(new ArrayList<Double>());

        for(ArrayList<Double> i:polynomial){
            for(int j=0; j<maxExp+1;j++){
                i.add(0.0);
            }
        }
        ////Log.d("Debug_main" , "polynomial before: " + polynomial);
        int ind1=0;
        for(ArrayList<ArrayList<Double>> side:equationExpForm){
            int ind2=0;
            for(ArrayList<Double> term:side){
                polynomial.get(ind1).set(term.get(1).intValue(), term.get(0)+polynomial.get(ind1).get(term.get(1).intValue()));
                ind2++;
            }
            ind1++;
        }
        return polynomial;
    }

    private String polynomialToString(ArrayList<ArrayList<Double>> polynomial){

        ArrayList<ArrayList<Double>> polynomial_cp = polynomial;
        StringBuilder polynomialStr = new StringBuilder();
        int ind1 = 0;
        for(ArrayList<Double> side:polynomial_cp){
            //int ind2 = side.size()-1;
            int ind2 =0;
            boolean doPlus = false;
            //Collections.reverse(side);

            ////Log.d("Debug_polynomial",polynomial_cp+"");
            for(Double term:side){

                if(term != 0.0){
                    if(doPlus){
                        polynomialStr.append("+");
                    }else{
                        doPlus=true;
                    }
                    if(ind2==0){
                        polynomialStr.append(floatToString(term));
                    }else if(ind2==1){
                        polynomialStr.append(floatToString(term));
                        polynomialStr.append("x");
                    }else{
                        polynomialStr.append(floatToString(term));
                        polynomialStr.append("x^");
                        polynomialStr.append(ind2);
                    }


                }
                ind2++;
            }
            ind1++;
            String toStr = polynomialStr.toString();
            ////Log.d("debug_empty", toStr);
            ////Log.d("debug_empty", Arrays.asList(toStr.split("="))+" , ind1=" + ind1);
            if( Arrays.asList(toStr.split("=")).size() != ind1 ){
                polynomialStr.append("0");
            }
            if(ind1 != polynomial.size()){
                polynomialStr.append("=");
            }


        }

        return polynomialStr.toString();

    }

    private List<String> solveEquation(String equationInp, int maxExp){

        List<String> returnVals = new ArrayList();

        String returnVal1 = equationInp;

        ArrayList<ArrayList<ArrayList<String>>> equationSplitByTimes = startOps(equationInp);

        ArrayList<ArrayList<ArrayList<Double>>> equationExpForm = simplifyMult(equationSplitByTimes);
        String returnVal2 =  expToString(equationExpForm);

        boolean didChange = false;

        for( ArrayList<ArrayList<Double>> side:equationExpForm){
            for( ArrayList<Double> term:side){
                if( (term.get(0) % term.get(2) == 0)){
                    didChange = true;
                    term.set(0, term.get(0)/term.get(2));
                    term.set(2, Double.valueOf(1));
                }else if((term.get(2) % term.get(0) == 0)){
                    didChange = true;
                    term.set(2, term.get(2)/term.get(0));
                    term.set(0, Double.valueOf(1));
                }
            }
        }
        String returnVal3 = expToString(equationExpForm);

        equationExpForm = rmvDivision(equationExpForm);
        String returnVal4 = expToString(equationExpForm);




        ArrayList<ArrayList<Double>> polynomial = combineTerms(equationExpForm,maxExp);
        //Log.d("Debug_main", "polynomial: " + polynomial);
        String returnVal5 = polynomialToString(polynomial);

        //Log.d("Debug_main", "polynomialStr: " + returnVal5);


        Double x0 = polynomial.get(1).get(1);
        Double c0 = polynomial.get(1).get(0);
        Double x1 = polynomial.get(0).get(1);
        Double c1 = polynomial.get(0).get(0);
        Double sqr0 = polynomial.get(1).get(2);
        Double sqr1 = polynomial.get(0).get(2);

        //Log.d("Debug_polynomial", sqr0 + "x^2" + x0 + "x" + c0  +"=" + sqr0 + "x^2" + x0 + "x" + c0 + "=");

        String returnVal6 = "";
        String returnVal7 = "";
        String returnVal8 = "";
        String returnVal9 = "";
        String returnVal10 = "";
        if(sqr0-sqr1 == 0) {


            //Log.d("Debug_polynomial", "option 1");


            x1 = x1 - x0;
            x0 = 0.0;

            ////Log.d("Debug_equation", c0 + "x+" + x0 + "=" + c1 + "x+" + x1 );

            polynomial.get(1).set(0, c0);
            polynomial.get(1).set(1, x0);
            polynomial.get(1).set(2, sqr0);
            polynomial.get(0).set(0, c1);
            polynomial.get(0).set(1, x1);
            polynomial.get(0).set(2, sqr1);

            returnVal6 = polynomialToString(polynomial);

            c0 = c0 - c1;
            c1 = 0.0;
            polynomial.get(1).set(0, c0);
            polynomial.get(1).set(1, x0);
            polynomial.get(1).set(2, sqr0);
            polynomial.get(0).set(0, c1);
            polynomial.get(0).set(1, x1);
            polynomial.get(0).set(2, sqr1);

            returnVal7 = polynomialToString(polynomial);

            sqr0 = sqr0 - sqr1;
            sqr1 = 0.0;

            polynomial.get(1).set(0, c0);
            polynomial.get(1).set(1, x0);
            polynomial.get(1).set(2, sqr0);
            polynomial.get(0).set(0, c1);
            polynomial.get(0).set(1, x1);
            polynomial.get(0).set(2, sqr1);

            returnVal8 = polynomialToString(polynomial);

            if (x1 == 0.0) {
                if (c0 == 0.0) {
                    returnVal9 = "all real numbers";
                } else {
                    returnVal9 = "no real solution";
                }
            } else {
                returnVal9 = "x=" + (floatToString(c0 / x1));
            }
        }else{
            //Log.d("Debug_polynomial", "option 2");

            x1 = x1 - x0;
            x0 = 0.0;

            ////Log.d("Debug_equation", c0 + "x+" + x0 + "=" + c1 + "x+" + x1 );

            polynomial.get(1).set(0, c0);
            polynomial.get(1).set(1, x0);
            polynomial.get(1).set(2, sqr0);
            polynomial.get(0).set(0, c1);
            polynomial.get(0).set(1, x1);
            polynomial.get(0).set(2, sqr1);

            returnVal6 = polynomialToString(polynomial);

            c1 = c1 - c0;
            c0 = 0.0;

            polynomial.get(1).set(0, c0);
            polynomial.get(1).set(1, x0);
            polynomial.get(1).set(2, sqr0);
            polynomial.get(0).set(0, c1);
            polynomial.get(0).set(1, x1);
            polynomial.get(0).set(2, sqr1);

            returnVal7 = polynomialToString(polynomial);

            sqr1 = sqr1 - sqr0;
            sqr0 = 0.0;

            polynomial.get(1).set(0, c0);
            polynomial.get(1).set(1, x0);
            polynomial.get(1).set(2, sqr0);
            polynomial.get(0).set(0, c1);
            polynomial.get(0).set(1, x1);
            polynomial.get(0).set(2, sqr1);

            returnVal8 = polynomialToString(polynomial);

            char plusMinus = (char) 241;
            char sqrt = (char) 251;

            //Log.d("debug_str", "" + sqrt + plusMinus);

            returnVal9 = String.format("(-%s±√(%s^2-4*%s*%s))/2*%s", floatToString(x1), floatToString(x1), floatToString(sqr1), floatToString(c1),floatToString(sqr1));
            Double discriminant = Math.pow(x1, 2)-4*sqr1*c1;
            //Log.d("Debug_polynomial", "discrimiminant: " + discriminant);
            if(discriminant>0.0){
                Double solution1 = (-x1+Math.sqrt(discriminant))/(2*sqr1);
                Double solution2 = (-x1-Math.sqrt(discriminant))/(2*sqr1);
                returnVal10 = floatToString(solution1) + " or " + floatToString(solution2);
            }else if(discriminant==0.0){
                Double solution = -x1/2*sqr1;
                returnVal10 = floatToString(solution);
            }else{
                returnVal10 = "no real solution (x is a complex number)";
            }
        }

        returnVals.add(returnVal1);
        if(!returnVal1.equals(returnVal2)){
            returnVals.add(returnVal2);
        }
        if(!returnVal2.equals(returnVal3)){
            returnVals.add(returnVal3);
        }
        if(!returnVal3.equals(returnVal4)){
            returnVals.add(returnVal4);
        }
        if(!returnVal4.equals(returnVal5)){
            returnVals.add(returnVal5);
        }
        if(!returnVal5.equals(returnVal6)){
            returnVals.add(returnVal6);
        }
        if(!returnVal6.equals(returnVal7)){
            returnVals.add(returnVal7);
        }
        if(!returnVal7.equals(returnVal8)){
            returnVals.add(returnVal8);
        }
        if(!returnVal8.equals(returnVal9)){
            returnVals.add(returnVal9);
        }
        if(!returnVal9.equals(returnVal10)){
            returnVals.add(returnVal10);
        }
        return returnVals;
    }

    private Button submit;
    public static final String EXTRA_MESSAGE = "com.knaanstudios.easyalgebra.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText equationSource = (EditText) findViewById(R.id.equation);
                final Context ctx = getApplication();
                final TextView output = (TextView) findViewById(R.id.solution);
                String equation = equationSource.getText().toString();

                List<String> equationSolved;
                String equationSolved_str = "\n";
                try {

                    equationSolved = solveEquation(equation, 2);
                }catch (Throwable e){
                    equationSolved = new ArrayList<String>();
                    equationSolved.add(getString(R.string.error_message));
                    //Log.e("MYAPP", "exception", e);
                    //equationSolved.add(e.getLocalizedMessage());

                }

                Intent intent = new Intent(ctx, SolutionActivity.class);
                intent.putExtra(EXTRA_MESSAGE, equationSolved.toArray(new String[equationSolved.size()]));
                startActivity(intent);
            }
        });


    }


}


