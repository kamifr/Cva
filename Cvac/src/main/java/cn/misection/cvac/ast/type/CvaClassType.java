package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:45:00
 */
public final class CvaClassType extends AbstractType
{
    public static final String TYPE_LITERAL = "@class";

    private String literal;

    public CvaClassType(String literal)
    {
        this.literal = literal;
    }

    @Override
    public String toString()
    {
        return String.format("@class:%s", literal);
    }

    public String getLiteral()
    {
        return literal;
    }
}
