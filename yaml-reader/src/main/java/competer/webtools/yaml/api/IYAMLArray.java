package competer.webtools.yaml.api;

import java.util.Iterator;

public interface IYAMLArray extends IYAMLArtifact
{
    int size();
    <T> boolean forEach( Class<T> _class , IYAMLArrayElementProcessor<T> _processor  );
    Iterator<?> iterator();
}
