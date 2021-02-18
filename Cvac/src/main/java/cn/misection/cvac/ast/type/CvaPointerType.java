package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaPointer
 * @Description TODO
 * @CreateTime 2021年02月16日 16:15:00
 */
public final class CvaPointerType extends AbstractUnsafe
{
    public static final String TYPE_LITERAL = "@pointer";

    public CvaPointerType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
