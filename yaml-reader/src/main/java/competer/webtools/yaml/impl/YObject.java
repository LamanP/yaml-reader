package competer.webtools.yaml.impl;

import java.util.Iterator;
import java.util.Map;

import competer.webtools.yaml.api.IYAMLObject;

public class YObject extends YArtifact implements IYAMLObject
{

    private final   Map<?, ?> m_map;

    YObject( YArtifact _parent, Object _address, Map<?,?> _map )
    {
        super( _parent, _address );
        m_map = _map;
    }

    @Override
    public Iterator<?> addressIterator()
    {
        return m_map.keySet().iterator();
    }
    @Override
    public Object getRaw( Object _address )
    {
        return m_map.get( _address );
    }

    @Override
    public boolean has( Object _address )
    {
        return m_map.containsKey( _address );
    }

    @Override
    public boolean isNull( Object _address )
    {
        return ( m_map.get( _address ) == null );
    }

    @Override
    public String toString()
    {
        return m_map.toString();
    }

    @Override
    protected Object getRawData()
    {
        return m_map;
    }
}
