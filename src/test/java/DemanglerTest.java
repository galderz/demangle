import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DemanglerTest
{
//    // todo test `a` primitive
//    // todo test `b` primitive
//    // todo test `t` primitive
//
    @Test
    public void testJavaMethodComplex()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin115regionMatchesCIEJbP6byte[]iP6byte[]ii")
            , equalTo("bool java.lang.StringLatin1::regionMatchesCI(byte[]*, int, byte[]*, int, int)")
        );
    }

    @Test
    public void testJavaMethodStringByteArray()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJP16java.lang.StringP6byte[]")
            , equalTo("java.lang.String* java.lang.StringLatin1::newString(byte[]*)")
        );
    }

    @Test
    public void testJavaMethodStringVoid()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJP16java.lang.Stringv")
            , equalTo("java.lang.String* java.lang.StringLatin1::newString()")
        );
    }

    @Test
    public void testJavaMethodDoubleDouble()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJdd")
            , equalTo("double java.lang.StringLatin1::newString(double)")
        );
    }

    @Test
    public void testJavaMethodFloatFloat()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJff")
            , equalTo("float java.lang.StringLatin1::newString(float)")
        );
    }

    @Test
    public void testJavaMethodIntInt()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJii")
            , equalTo("int java.lang.StringLatin1::newString(int)")
        );
    }

    @Test
    public void testJavaMethodIntIntInt()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJiii")
            , equalTo("int java.lang.StringLatin1::newString(int, int)")
        );
    }

    @Test
    public void testJavaMethodLongLong()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJll")
            , equalTo("long java.lang.StringLatin1::newString(long)")
        );
    }

    @Test
    public void testJavaMethodShortShort()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJss")
            , equalTo("short java.lang.StringLatin1::newString(short)")
        );
    }

    @Test
    public void testJavaMethodVoidVoid()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringEJvv")
            , equalTo("void java.lang.StringLatin1::newString()")
        );
    }

    @Test
    public void testNamespace()
    {
        assertThat(
            demangle("_ZN22java.lang.StringLatin19newStringE")
            , equalTo("java.lang.StringLatin1::newString")
        );
    }

    @Test
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
    public void testIncompleteNestName()
    {
        assertThat(
            demangle("_Z6Str")
            , equalTo("_Z6Str")
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
