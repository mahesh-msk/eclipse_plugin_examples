/**
 * 
 */
package cantsyn;


/**
 * @author TJS5KOR
 *
 */
import com.bosch.bct.javaaction.context.interfaces.IContext;
import com.bosch.bct.javaaction.context.interfaces.ILog;
import ecucvalues.CanTSyn;
import ecucvalues.CanTSyn.CanTSynGlobalTimeDomain;
import ecucvalues.EcucValues;

import com.bosch.bct.javaaction.context.interfaces.IJavaAction;


public class CanTSynPrepareAction implements IJavaAction {                                       
	 @Override

    public void run(IContext context) {
	
		ILog log  = context.getLog(); 
		EcucValues ecucvalues = context.getModel(EcucValues.class);
		CanTSyn cantsyn = ecucvalues.getCanTSyn();		
		 
	    if(cantsyn != null){
	    	
	    	CanTSynForwardIds(cantsyn,context);	    	
	    }
	    else 
	    {
	    	log.info("[CANTSYN] CanTSyn is not configured. Nothing to forward.");
	    }	    
	 }
	 
	 private void CanTSynForwardIds(CanTSyn cantsyn, IContext context) { 
		 
		 ILog log  = context.getLog(); 
		 int CanTSynGlobalTimeMasterConfirmationHandleIdCtr = 0;
		 int CanTSynGlobalTimeSlaveHandleIdCtr = 0;
		 		 
		 if(cantsyn.getCanTSynGlobalTimeDomains() != null)
		 {
			 
			 try {				 	 
					// Iterating on all Global Time domains
					 for (CanTSynGlobalTimeDomain gtd: cantsyn.getCanTSynGlobalTimeDomains())
					 {
						 if(gtd.getCanTSynGlobalTimeMaster() !=null)
						 {
							 // Forward unique IDs to CanTSynGlobalTimeMasterConfirmationHandleId
						     gtd.getCanTSynGlobalTimeMaster().getCanTSynGlobalTimeMasterPdu().createCanTSynGlobalTimeMasterConfirmationHandleId(CanTSynGlobalTimeMasterConfirmationHandleIdCtr);
						     CanTSynGlobalTimeMasterConfirmationHandleIdCtr++;
						 }
						 if(gtd.getCanTSynGlobalTimeSlave() !=null)
						 {
							 // Forward unique IDs to CanTSynGlobalTimeSlaveHandleId
						     gtd.getCanTSynGlobalTimeSlave().getCanTSynGlobalTimeSlavePdu().createCanTSynGlobalTimeSlaveHandleId(CanTSynGlobalTimeSlaveHandleIdCtr);
						     CanTSynGlobalTimeSlaveHandleIdCtr++;
						 }						
					 }
	
			}
			 catch(NullPointerException e)
			 {
				 log.error("Null pointer exception occured. Forwarding not executed.");
			 }
		 }
		 else
		 {
			 log.error("[CANTSYN][CanTSynGlobalTimeDomain] CanTSynGlobalTimeDomain not available. Forwarding not executed.");
		 }
	
	 }
	 
}
