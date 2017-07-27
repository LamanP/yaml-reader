package competer.webtools.yaml.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import competer.webtools.yaml.api.IYAMLAddressResolver;
import competer.webtools.yaml.api.IYAMLArray;
import competer.webtools.yaml.api.IYAMLArtifact;
import competer.webtools.yaml.api.IYAMLObject;
import competer.webtools.yaml.api.YAMLException;

abstract class YArtifact implements IYAMLArtifact
{
    private final   YArtifact   m_parent;
    private final   Object      m_key;

    protected class YAddressResolver implements IYAMLAddressResolver
    {
        public  final   YArtifact       container;
        public  final   Object          address;

        public YAddressResolver( Object... _address )
        {
            container = getContainer( _address );
            address = _address[_address.length - 1];
        }

        private YAddressResolver( YArtifact _container, Object _address )
        {
            container   = _container;
            address     = _address;
        }

        private YArtifact getContainer( Object... _address )
        {
            YArtifact ret = YArtifact.this;
            YArtifact valueParent = null; // Used if we navigate to a value
            for ( int i = 0; i < _address.length; i++ )
            {
                if ( _address[i].equals( ".." ) ) // Back one level
                {
                    if ( ret == null )
                        ret = valueParent;
                    else
                        ret = ret.m_parent;
                }
                else if ( _address[i].equals( "." ) )
                    ; // Same level
                else if ( ret == null ) // Trying to go beyond a value
                    throw new YAMLException( "Attempt to navigate under a value", valueParent.path(), this, null );
                else
                {
                    if ( ret.has( _address[i] ) )
                    {
                        Object value = ret.getRaw( _address[i] );
                        if ( value instanceof Map<?,?> )
                            ret = new YObject( ret, _address[i], (Map<?, ?>)value );
                        else if ( value instanceof List<?> )
                            ret = new YArray( ret, _address[i], (List<?>)value );
                        else
                        {
                            valueParent = ret;
                            ret = null;
                        }
                    }
                    else if ( i < _address.length - 1 )
                        throw ret.requiredElementMissingError( _address[i] );
                    else
                        return ret;
                }
            }
            if ( ret == null )
                return valueParent;
            else
                return ret.m_parent;
        }

        @Override
        public String fullPath()
        {
            return container.path() + "/" + address.toString();
        }
    }

    YArtifact( YArtifact _parent, Object _key )
    {
        m_parent    = _parent;
        m_key       = _key;
    }

    @Override
    public Object key()
    {
        return m_key;
    }

    @Override
    public Object key( Object... _address )
    {
        if ( _address.length == 0 )
            throw new YAMLException( "Address may not be of length 0", path(),
                new YAddressResolver( this, _address ), null );
        Object target = get( Object.class, true, _address );
        if ( target instanceof YArtifact )
            return ( (YArtifact)target ).m_key;
        else
            return last( _address );
    }

    protected Object last( Object[] _address )
    {
        return _address[_address.length - 1];
    }

    @Override
    public IYAMLArtifact parent()
    {
        return m_parent;
    }

    @Override
    public String path()
    {
        StringBuilder sb = new StringBuilder();
        buildPath( sb );
        return sb.toString();
    }

    private void buildPath( StringBuilder sb )
    {
        if ( m_parent != null )
        {
            m_parent.buildPath( sb );
            if ( sb.length() > 0 )
                sb.append( '/' );
        }
        if ( m_key != null )
            sb.append( m_key );
    }

    @SuppressWarnings( "unchecked" )
    protected <T> T cast( Class<T> _class, IYAMLAddressResolver _addressResolver, Object _value )
    {
        try
        {
            // If Long is requested, but Integer given, convert it
            if ( _class == Long.class && _value instanceof Integer )
            {
                Integer intValue = (Integer)_value;
                return (T)new Long( intValue.longValue() );
            }
            return (T)_value;
        }
        catch ( ClassCastException cce )
        {
            throw mapCastingError( _class, _addressResolver, _value, cce );
        }
    }

    protected <T> YAMLException mapCastingError(
        Class<T> _correctClass, IYAMLAddressResolver _addressResolver, Object _actualValue,
        Throwable _cause )
    {
        return new YAMLException(
            "Value \"%s\" is not of required class \"%s\", but of class \"%s\".", path(),
            _addressResolver, _cause, _actualValue, _correctClass.getName(), _actualValue
                .getClass().getName() );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public <T> T get( Class<T> _class , boolean _required , Object... _address  )
    {
        YAddressResolver ca = new YAddressResolver( _address );
        if ( !ca.container.has( ca.address ) )
        {
            if ( _required )
                throw new YAMLException( "Object has no such property/element", path(), ca, null );
            else
                return null;
        }
        Object value = ca.container.getRaw( ca.address );
        if ( value == null )
            return null;

        // Special class cases...
        if ( value instanceof List<?> )
            return (T)new YArray( ca.container, ca.address, cast( List.class, ca, value ) );
        else if ( value instanceof Map<?, ?> )
            return (T)new YObject( ca.container, ca.address, cast( Map.class, ca, value ) );
        return cast( _class, ca, value );
    }

    @Override
    public void require( Object _address )
    {
        if ( !has( _address ) )
            throw requiredElementMissingError( _address );
    }

    private YAMLException requiredElementMissingError( Object _address )
    {
        return new YAMLException( "A property/element is required at this address", path(), new YAddressResolver( this, _address ), null );
    }

    @Override
    public Iterator<IYAMLArtifact> propertyBrowser( Object _address )
    {
        return new YPropertyBrowser( this, _address );
    }

    @Override
    public IYAMLArray ar( Object... _address )
    {
        return get( IYAMLArray.class, true, _address );
    }

    @Override
    public IYAMLArray oar( Object... _address )
    {
        return get( IYAMLArray.class, false, _address );
    }

    @Override
    public IYAMLObject ob( Object... _address )
    {
        return get( IYAMLObject.class, true, _address );
    }

    @Override
    public IYAMLObject oob( Object... _address )
    {
        return get( IYAMLObject.class, false, _address );
    }

    @Override
    public String str( Object... _address )
    {
        return get( String.class, true, _address );
    }

    @Override
    public String ostr( Object... _address )
    {
        return get( String.class, false, _address );
    }

    @Override
    public String ostrDefault( String _defaultValue, Object... _address )
    {
        String ret = get( String.class, false, _address );
        if ( ret == null )
            return _defaultValue;
        return ret;
    }

    @Override
    public long longNum( Object... _address )
    {
        return get( Long.class, true, _address );
    }

    @Override
    public Long olongNum( Object... _address )
    {
        return get( Long.class, false, _address );
    }

    @Override
    public Long olongNumDefault( Long _defaultValue, Object... _address )
    {
        Long ret = get( Long.class, false, _address );
        if ( ret == null )
            return _defaultValue;
        return ret;
    }

    @Override
    public int intNum( Object... _address )
    {
        return get( Integer.class, true, _address );
    }

    @Override
    public Integer ointNum( Object... _address )
    {
        return get( Integer.class, false, _address );
    }

    @Override
    public Integer ointNumDefault( Integer _defaultValue, Object... _address )
    {
        Integer ret = get( Integer.class, false, _address );
        if ( ret == null )
            return _defaultValue;
        return ret;
    }

    @Override
    public boolean bool( Object... _address )
    {
        return get( Boolean.class, true, _address );
    }

    @Override
    public Boolean obool( Object... _address )
    {
        return get( Boolean.class, false, _address );
    }

    @Override
    public Boolean oboolDefault( Boolean _defaultValue, Object... _address )
    {
        Boolean ret = get( Boolean.class, false, _address );
        if ( ret == null )
            return _defaultValue;
        return ret;
    }

    protected abstract Object getRawData();
}
