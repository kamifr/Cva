class TestMain
{
    // This is the entry point of the program
    int main()
    {
        echo(new Test().Compute(10));   // just a print statement;
        //echo(1);
    }
}

class Test
{
    int Compute(int num)
    {
        int total;
        if ( num < 1)
        {
            total = 1;
        }
        else
        {
            total = num * (this.Compute(num-1));
        }
        return total;
    }
}