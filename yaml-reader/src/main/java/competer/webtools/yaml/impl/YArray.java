package competer.webtools.yaml.impl;

import java.util.Iterator;
import java.util.List;

import competer.webtools.yaml.api.IYAMLArray;
import competer.webtools.yaml.api.IYAMLArrayElementProcessor;

class YArray extends YArtifact implements IYAMLArray
{

    private final   List<?> m_list;

    YArray( YArtifact _parent, Object _address, List<?> _list )
    {
        super( _parent, _address );
        m_list = _list;
    }

    @Override
    public int size()
    {
        return m_list.size();
    }
    @Override
    public Object getRaw( Object _address )
    {
        return m_list.get( (Integer )_address );
    }

    @Override
    public boolean has( Object _address )
    {
        if ( _address instanceof Integer )
        {
            int index = (Integer)_address;
            return ( ( index >= 0 ) && (index < m_list.size() ) );
        }
        else
            return false;
    }

    @Override
    public boolean isNull( Object _address )
    {
        int index = (Integer)_address;
        return ( ( index < 0 ) || (index >= m_list.size() ) || m_list.get( index ) == null );
    }

    @Override
    public String toString()
    {
        return m_list.toString();
    }

    @Override
    public <T> boolean forEach( Class<T> _class , IYAMLArrayElementProcessor<T> _processor  )
    {
        for ( int i = 0; i < m_list.size(); i++ )
        {
            T elt = get( _class, true, i );
            if ( !_processor.process( i, elt ) )
                return false;
        }
        return true;
    }

    @Override
    public Iterator<?> iterator()
    {
        return m_list.iterator();
    }

    @Override
    protected Object getRawData()
    {
        return m_list;
    }

    @Override
    public Iterator<?> addressIterator()
    {
        return new YArrayIndexIterator( 0, m_list.size() - 1 );
    }
}
