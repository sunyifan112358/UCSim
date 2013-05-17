package ucsim.core.node.protocol.datalinkprotocol;


import ucsim.core.node.protocol.Protocol;
import ucsim.core.packet.Packet;

/**
 * @author yifan
 *
 */
public class DatalinkProtocol extends Protocol {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * process input data, generate output data
     */
    @Override
    public void process(){
        
    }

	/* (non-Javadoc)
	 * @see ucsim.core.node.protocol.Protocol#processOnePacketFromLowerLayer(ucsim.core.packet.Packet)
	 */
	@Override
	protected void processOnePacketFromLowerLayer(Packet p) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ucsim.core.node.protocol.Protocol#processOnePacketFromHigherLayer(ucsim.core.packet.Packet)
	 */
	@Override
	protected void processOnePacketFromHigherLayer(Packet p) {
		// TODO Auto-generated method stub
		
	}
   

}
