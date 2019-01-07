package com.uber.piranha;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

class XPFlagCleanerNegativeCases {

  enum TestExperimentName {
    RANDOM_FLAG
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD})
  @interface ToggleTesting {
    TestExperimentName[] treated();
  }

  private XPTest experimentation;

  private boolean tBool = false;

  private boolean tBool1 = false;
  private boolean tBool2 = false;

  public void conditional_contains_nonstale_flag() {
    if(experimentation.isFlagTreated(TestExperimentName.RANDOM_FLAG)) {
      System.out.println("Hello World");
    }
  }

  public void conditional_with_else_contains_nonstale_flag() {
    if(experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG)) {
      System.out.println("Hello World");
    } else {
      System.out.println("Hi world");
    }
  }

  public void complex_conditional_contains_nonstale_flag() {
    if(tBool1 || (tBool2 && experimentation.isToggleEnabled(TestExperimentName
        .RANDOM_FLAG))) {
      System.out.println("Hello World");
    } else {
      System.out.println("Hi world");
    }
  }

  public void assignments_containing_nonstale_flag() {
    tBool = experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG);

    tBool = experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG) && tBool;

    tBool = experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG) || tBool;

    tBool = experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG) && (tBool1
        || tBool2);

  }

  public boolean return_contains_nonstale_flag() {
    return experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG);
  }


  public void condexp_contains_nonstale_flag() {
    tBool =  experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG) ? true : false;
  }

  public void misc_xp_apis_containing_nonstale_flag() {
    if(experimentation.isToggleEnabled(TestExperimentName.RANDOM_FLAG) && (tBool1 ||
        tBool2)) {}

    experimentation.putToggleEnabled(TestExperimentName.RANDOM_FLAG);

    experimentation.putToggleDisabled(TestExperimentName.RANDOM_FLAG);

    if(experimentation.isToggledDisabled(TestExperimentName.RANDOM_FLAG) &&
        (tBool1 || tBool2)) {}
  }

  @ToggleTesting(treated = TestExperimentName.RANDOM_FLAG)
  public void annotation_test() {}


  class XPTest {
    public boolean isToggleEnabled(TestExperimentName x) { return true; }
    public boolean putToggleEnabled(TestExperimentName x) { return true; }
    public boolean isToggledDisabled(TestExperimentName x) { return true; }
    public boolean isFlagTreated(TestExperimentName x) { return true; }
    public boolean putToggleDisabled(TestExperimentName x) { return true; }
  }
}