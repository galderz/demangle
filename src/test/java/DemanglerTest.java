import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DemanglerTest
{
    @Test
    @Disabled
    public void testNamespace()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringE")
            , equalTo("java.lang.StringLatin1::newString")
        );
    }

    @Test
    @Disabled
    public void testIncompleteNamespace()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin1")
            , equalTo("_ZN22java.lang.StringLatin1")
        );
    }

    @Test
    public void testNestName()
    {
        assertThat(
            demangle("_Z6String")
            , equalTo("String")
        );
        assertThat(
            demangle("_Z22java.lang.StringLatin1")
            , equalTo("java.lang.StringLatin1")
        );
    }

    @Test
    public void testInvalidMagic()
    {
        assertThat(demangle(""), equalTo(""));
        assertThat(demangle("n"), equalTo("n"));
        assertThat(demangle("_"), equalTo("_"));
        assertThat(demangle("_Z"), equalTo("_Z"));
    }

    private static String demangle(String input)
    {
        return new demangle.Demangler()
            .demangle(input);
    }
}
