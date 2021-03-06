package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Context;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    Context context;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testUser(){
        User user = new User("Robin", "0739163401", "robin_wilde@hotmail.com", true, true, true);
        String iUser = user.toString();
    }
    @Test
    public void testTimeReportObject(){
        TimeReport tr = new TimeReport("2017", "10", "27", "42", "Robin","guid-123123", "DHL", "Göteborg",
                "9", false, "Testade att köra", false, "Max", "2017-10-27");
        String iTr = tr.toString();
    }
    @Test
    public void testYearMonthDayWeek(){
        HelpMethods.splitYearMonthDay("2017-11-06");
    }
    @Test
    public void testDayTrimmer(){
        HelpMethods.trimDayStringIfStartWithZero("06");
    }
    @Test
    public void testTimeStampSpllitter(){
        HelpMethods.getDateFromPalletReportTimeStamp("2017-11-20 20:14:05");
    }

}