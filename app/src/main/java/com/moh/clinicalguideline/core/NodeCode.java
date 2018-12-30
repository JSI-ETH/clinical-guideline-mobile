package com.moh.clinicalguideline.core;

public class NodeCode {
    public static String ADULT_SYMPTOM ="ASMPT";
    public static String CHILD_SYMPTOM ="CSMPT";
    public static String CHRONIC_CARE ="CHRNC";
    public static String URGENT ="URGNT";
    public static String NOT_URGENT ="NTURG";
    public static String ALGORITHM ="ALGTM";
    public static boolean isSymptomOrCare(String nodeCode){
        return nodeCode.equalsIgnoreCase(ADULT_SYMPTOM)
                || nodeCode.equalsIgnoreCase(CHILD_SYMPTOM)
                || nodeCode.equalsIgnoreCase(CHRONIC_CARE);
    }
    public static boolean isUrgent(String nodeCode){
        return  nodeCode.equalsIgnoreCase(URGENT);
    }
    public static boolean isNonUrgent(String nodeCode){
        return nodeCode.equalsIgnoreCase(NOT_URGENT);
    }
    public static boolean isAlgorithm(String nodeCode){
        return nodeCode.equalsIgnoreCase(ALGORITHM);
    }
}
