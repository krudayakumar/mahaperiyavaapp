package com.mahaperivaya.Model;

/**
 * Created by m84098 on 9/22/15.
 */
public class Countries {
    private static Country[] countries;







    public Country[] getCountries ()
    {
        return countries;
    }

    public void setCountries (Country[] countries)
    {
        this.countries = countries;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [countries = "+countries+"]";
    }
}
