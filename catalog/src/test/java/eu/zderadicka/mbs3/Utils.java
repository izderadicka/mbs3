package eu.zderadicka.mbs3;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

import org.hamcrest.Matcher;

public class Utils {

    public static Matcher<Integer> isSuccessNoRedirect() {
        return allOf(greaterThanOrEqualTo(200), lessThan(300));
    }

}
