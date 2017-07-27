package competer.webtools.yaml.impl;

import java.util.ArrayList;
import java.util.Iterator;

import competer.webtools.yaml.api.IYAMLArtifact;

/**
 * This iterator browses through all descendant objects of a Map and List that are Map or List
 * themselves and that contain the specified property name or index. The results a YAML artifacts.
 */
class YPropertyBrowser implements Iterator<IYAMLArtifact>
{
    private class YArtifactWithIterator
    {
        public       Iterator<?>    it;
        public final YArtifact      artifact;

        public YArtifactWithIterator( YArtifact _artifact )
        {
            artifact    = _artifact;
            it          = null;
        }

        public void beginIteration()
        {
            it = artifact.addressIterator();
        }

        @Override
        public String toString()
        {
            String hasNext;
            if ( it == null )
                hasNext = "maybe";
            else if ( it.hasNext() )
                hasNext = "yes";
            else
                hasNext = "no";
            return String.format( "hasNext(): %s; artifact: %s", hasNext, artifact.toString() );
        }
    }

    private final   Object                              m_address;
    private final   ArrayList<YArtifactWithIterator>    m_backlog;
    private         int                                 m_backlogIndex;
    private         YArtifact                           m_next;

    YPropertyBrowser( YArtifact _rootArtifact, Object _address  )
    {
        m_address = _address;
        m_backlog = new ArrayList<YArtifactWithIterator>();
        m_backlog.add( new YArtifactWithIterator( _rootArtifact ) );
        m_backlogIndex = 0;
        m_next = null;
        findNext();
    }

    private void findNext()
    {
        m_next = null;
        while ( m_next == null && m_backlogIndex >= 0 )
        {
            YArtifactWithIterator item = m_backlog.get( m_backlogIndex );
            if ( item.it == null ) // Item not yet being processed
            {
                item.beginIteration();
                iterateChildren( item );
                if ( item.artifact.has( m_address ) )
                    m_next = item.artifact;
            }
            else if ( item.it.hasNext() )// Continue iteration for this item
            {
                iterateChildren( item );
            }
            else // No more children, go back one level
                m_backlogIndex--;
        }
    }

    protected void iterateChildren( YArtifactWithIterator _item )
    {
        boolean found = false;
        while ( _item.it.hasNext() && !found )
        {
            Object firstChild = _item.artifact.get( Object.class, true, _item.it.next() );
            if ( firstChild instanceof YArtifact )
            {
                addToBacklog( firstChild );
                found = true;
            }
        }
    }

    protected void addToBacklog( Object _artifact )
    {
        YArtifactWithIterator item = new YArtifactWithIterator( (YArtifact)_artifact );
        if ( ++m_backlogIndex < m_backlog.size() )
            m_backlog.set( m_backlogIndex, item );
        else
        m_backlog.add( item );
    }

    @Override
    public boolean hasNext()
    {
        return ( m_next != null );
    }

    @Override
    public IYAMLArtifact next()
    {
        YArtifact nextArtifact = m_next;
        findNext();
        return nextArtifact;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
