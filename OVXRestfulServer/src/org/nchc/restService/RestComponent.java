package org.nchc.restService;

import org.restlet.Component;

public class RestComponent extends Component {
	
	public RestComponent() {
		super();
		getDefaultHost().attach("/opennsa", new OpennsaApplication());
	}
	

}
