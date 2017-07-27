package competer.webtools.yaml.impl;

import java.util.Iterator;

public class YArrayIndexIterator implements Iterator<Integer>
{
    private         int m_index;
    private final   int m_hi;

    YArrayIndexIterator( int _lo, int _hi )
    {
        m_index = _lo;
        m_hi    = _hi;
    }

    @Override
    public boolean hasNext()
    {
        return ( m_index <= m_hi );
    }

    @Override
    public Integer next()
    {
        if ( m_index <= m_hi )
            return m_index++;
        else
            return null;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
