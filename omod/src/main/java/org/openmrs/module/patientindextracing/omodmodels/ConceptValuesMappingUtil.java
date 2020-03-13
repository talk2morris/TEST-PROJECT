/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientindextracing.omodmodels;

import javafx.util.Pair;

/**
 * @author MORRISON.I
 */
public class ConceptValuesMappingUtil {

    //HIV TEST RESULT
    public final static Pair<Integer, String> HIV_TEST_RESULT_DEFAULT = new Pair<>(165792, "0");
    public final static Pair<Integer, String> HIV_TEST_RESULT_POSITIVE = new Pair<>(1228, "Positive");//Reactive
    public final static Pair<Integer, String> HIV_TEST_RESULT_NEGATIVE = new Pair<>(1229, "Negative"); //Non-Reactive
    public final static Pair<Integer, String> HIV_FINAL_TEST_RESULT_POSITIVE = new Pair<>(703, "Positive");//Positive
    public final static Pair<Integer, String> HIV_FINAL_TEST_RESULT_NEGATIVE = new Pair<>(664, "Negative"); //Negative

    //HTS CLIENT INTAKE
    public final static Pair<Integer, String> SESSION_INDIVIDUAL = new Pair<>(165792, "Individual");
    public final static Pair<Integer, String> SESSION_COUPLE = new Pair<>(165789, "Couple");
    public final static Pair<Integer, String> SESSION_PREVIOUSLY_SELF_TESTED = new Pair<>(165885, "Previously self tested");

    //marital status
    public final static Pair<Integer, String> MARITAL_STATUS_NEVER_MARRIED = new Pair<>(1057, "Never married");
    public final static Pair<Integer, String> MARITAL_STATUS_MARRIED = new Pair<>(5555, "Married");
    public final static Pair<Integer, String> MARITAL_STATUS_DIVORCED = new Pair<>(1058, "Divorced");
    public final static Pair<Integer, String> MARITAL_STATUS_SEPARATED = new Pair<>(1056, "Separated");
    public final static Pair<Integer, String> MARITAL_STATUS_LIVING_PARTNER = new Pair<>(1060, "Living with partner");
    public final static Pair<Integer, String> MARITAL_STATUS_WIDOWED = new Pair<>(1059, "Widow/er");

    //index_identified
    public final static Pair<Integer, String> INDEX_CLIENT_IDENTIFIED_YES = new Pair<>(1065, "Yes");
    public final static Pair<Integer, String> INDEX_CLIENT_IDENTIFIED_NO = new Pair<>(1066, "No");

    //index type
    public final static Pair<Integer, String> INDEX_TYPE_BIOLOGICAL = new Pair<>(165796, "Biological");
    public final static Pair<Integer, String> INDEX_TYPE_SEXUAL = new Pair<>(165797, "Sexual");
    public final static Pair<Integer, String> INDEX_TYPE_SOCIAL = new Pair<>(165795, "Social");

    //previously tested for HIV Before
    public final static Pair<Integer, String> PREV_TESTED_NOT_PREVIOUSLY_TESTED = new Pair<>(165815, "Not Previously Tested");
    public final static Pair<Integer, String> PREV_TESTED_PREV_NEGATIVE = new Pair<>(165816, "Previously tested negative");
    public final static Pair<Integer, String> PREV_TESTED_PREV_POSITIVE_IN_CARE = new Pair<>(165817, "Previously tested positive in HIV Care");
    public final static Pair<Integer, String> PREV_TESTED_PREV_POSITIVE_IN_NOT_CARE = new Pair<>(165882, "Previously tested positive not in HIVCare");

    //setting
    public final static Pair<Integer, String> SETTING_VCT = new Pair<>(160539, "vct");
    public final static Pair<Integer, String> SETTING_TB = new Pair<>(160529, "tb");
    public final static Pair<Integer, String> SETTING_STI = new Pair<>(160546, "sti");
    public final static Pair<Integer, String> SETTING_FP = new Pair<>(5271, "fp");
    public final static Pair<Integer, String> SETTING_OPD = new Pair<>(160542, "opd");
    public final static Pair<Integer, String> SETTING_WARD = new Pair<>(161629, "ward");
    public final static Pair<Integer, String> SETTING_OUTREACH = new Pair<>(165788, "outreach_program");
    public final static Pair<Integer, String> SETTING_COMMUNITY = new Pair<>(165788, "community"); //used community for outreach
    public final static Pair<Integer, String> SETTING_STANDALONE = new Pair<>(165838, "standalone_hts");
    public final static Pair<Integer, String> SETTING_OTHERS = new Pair<>(5622, "others");

}
