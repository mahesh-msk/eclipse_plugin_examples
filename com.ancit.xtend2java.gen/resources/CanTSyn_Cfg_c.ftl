

/***************************************************************************************************
* Includes
***************************************************************************************************/
#include "CanTSyn.h"
#include "CanIf.h"
#include "CanTSyn_Prv.h"


#define CANTSYN_START_SEC_CONST_8
#include "CanTSyn_MemMap.h"


<#list canTSyn_GenerateCfg_c.canTSynGlobalTimeDomain.canTSynGlobalTimeSyncDataIDList as timeSyncDataIDList><#if timeSyncDataIDList??>

/* Array to hold DataIDListElements(index and value) of SYNC Messages */
CONST(uint8, AUTOMATIC) CanTSyn_SYNCDataIdlist_<#if timeSyncDataIDList.parent.canTSynGlobalTimeMaster??>${timeSyncDataIDList.parent.canTSynGlobalTimeMaster.shortName}</#if><#if timeSyncDataIDList.parent.canTSynGlobalTimeSlave??>${timeSyncDataIDList.parent.canTSynGlobalTimeSlave.shortName}</#if>[16]=
{
    <#list timeSyncDataIDList.canTSynGlobalTimeSyncDataIDListElement?sort_by("e.canTSynGlobalTimeSyncDataIDListIndex") as canTSynSyncDataIdList>
    ${canTSynSyncDataIdList.canTSynGlobalTimeSyncDataIDListValue}u<#if canTSynSyncDataIdList?is_last><#else>,</#if></#list>

};
</#if><#if timeSyncDataIDList?is_last><#else>,</#if></#list><#list canTSyn_GenerateCfg_c.canTSynGlobalTimeDomain.canTSynGlobalTimeFupDataIDList as timeFupDataIDList><#if timeFupDataIDList??>
/* Array to hold DataIDListElements(index and value) of FUP Messages */
CONST(uint8, AUTOMATIC) CanTSyn_FUPDataIdlist_<#if timeFupDataIDList.parent.canTSynGlobalTimeMaster??>${timeFupDataIDList.parent.canTSynGlobalTimeMaster.shortName}</#if><#if timeFupDataIDList.parent.canTSynGlobalTimeSlave??>${timeFupDataIDList.parent.canTSynGlobalTimeSlave.shortName}</#if>[16]=
{
    <#list timeFupDataIDList.canTSynGlobalTimeFupDataIDListElement?sort_by("e.canTSynGlobalTimeFupDataIDListIndex") as canTSynFupDataIdList>
    ${canTSynFupDataIdList.canTSynGlobalTimeFupDataIDListValue}u<#if canTSynFupDataIdList?is_last><#else>,</#if></#list>

};
</#if><#if timeFupDataIDList?is_last><#else>,</#if></#list>


#define CANTSYN_STOP_SEC_CONST_8
#include "CanTSyn_MemMap.h"



#define CANTSYN_START_SEC_VAR_CLEARED_8
#include "CanTSyn_MemMap.h"

<#list canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeDomainMaster().canTSynGlobalTimeMaster as timeDomainMaster>
VAR(uint8, AUTOMATIC) CanTSyn_MasterBuffer_${timeDomainMaster.shortName}[<#if timeDomainMaster.parent.canTSynUseExtendedMsgFormat??>16<#else>8</#if>];  /* Master Buffer for ${timeDomainMaster.shortName}*/
<#if timeDomainMaster?is_last><#else>,</#if></#list>




#define CANTSYN_STOP_SEC_VAR_CLEARED_8
#include "CanTSyn_MemMap.h"



#define CANTSYN_START_SEC_CONST_UNSPECIFIED
#include "CanTSyn_MemMap.h"

<#if canTSyn_GenerateCfg_c.isCanTSynGlobalTimeMasterConfigured()??>
/* Global Time Domain Master Configuration */
CONST(CanTSyn_Tx_Configuration_t, AUTOMATIC) CanTSyn_Master_Tx_a[${canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeMaster().size}u]=
{
    <#list canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeDomainMaster().canTSynGlobalTimeMaster as timeDomainMaster>
    {
        /* ${timeDomainMaster.parent.shortName}: ${timeDomainMaster.shortName} */
        &CanTSyn_MasterBuffer_${timeDomainMaster.shortName}[0], /* Master buffer */
        <#if timeDomainMaster.parent.canTSynGlobalTimeSyncDataIDList!>NULL_PTR<#else>&CanTSyn_SYNCDataIdlist_${timeDomainMaster.shortName}[0]</#if>, /* SyncData ID list */
        <#if timeDomainMaster.parent.canTSynGlobalTimeFupDataIDList!>NULL_PTR<#else>&CanTSyn_FUPDataIdlist_${timeDomainMaster.shortName}[0]</#if>,  /* FUP Data ID list */
        CANTSYN_${timeDomainMaster.canTSynGlobalTimeTxCrcSecured}, /* configured CRC */
        ${timeDomainMaster.parent.canTSynSynchronizedTimeBaseRef.stbMSynchronizedTimeBaseIdentifier}u, /* stbm Id */
        ${timeDomainMaster.canTSynGlobalTimeTxPeriod/canTSyn_GenerateCfg_c.canTSynGeneral.canTSynMainFunctionPeriod.toInteger()}u, /* Tx period in ticks */
        ${timeDomainMaster.canTSynMasterConfirmationTimeout/canTSyn_GenerateCfg_c.canTSynGeneral.canTSynMainFunctionPeriod.toInteger()}u, /* Tx confirmation period in ticks */
        ${timeDomainMaster.canTSynGlobalTimeDebounceTime/canTSyn_GenerateCfg_c.canTSynGeneral.canTSynMainFunctionPeriod.toInteger()}u, /* Global debounce time in Ticks */
        ${timeDomainMaster.canTSynCyclicMsgResumeTime/canTSyn_GenerateCfg_c.canTSynGeneral.canTSynMainFunctionPeriod.toInteger()}u,  /* CanTSynCyclicMsgResumeTime in Ticks*/
        ${timeDomainMaster.parent.canTSynGlobalTimeDomainId}u, /* domain Id */
        ${timeDomainMaster.canTSynGlobalTimeMasterPdu.canTSynGlobalTimePduRef.CanIf_Aspects_CanTSyngetCanIfTxControllerId()}u,   /* controller Id */
        ${timeDomainMaster.canTSynGlobalTimeMasterPdu.canTSynGlobalTimePduRef.CanIf_Aspects_CanTSyngetCanIfTxPduName()}, /* CanIf tx pdu Id */
        <#if timeDomainMaster.canTSynImmediateTimeSync??>"TRUE "<#else>"FALSE"</#if>,  /* ImmediateTimeSync Frame*/
        <#if timeDomainMaster.parent.canTSynUseExtendedMsgFormat??>"TRUE"<#else>"FALSE"</#if>  /* CanTSynUseExtendedMsgFormat TRUE or FALSE*/
        
   
        
    }<#if timeDomainMaster?is_last><#else>,</#if></#list>
};
</#if>


<#if canTSyn_GenerateCfg_c.isCanTSynGlobalTimeSlaveConfigured()??>
/* Global Time Domain Slave Configuration */
CONST(CanTSyn_Rx_Configuration_t, AUTOMATIC) CanTSyn_Slave_Rx_a[${canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeSlave().size}u]=
{
    <#list canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeDomainSlave().canTSynGlobalTimeSlave as timeDomainSlave>
    {
        /* ${timeDomainSlave.parent.shortName}: ${timeDomainSlave.shortName} */
        <#if timeDomainSlave.parent.canTSynGlobalTimeSyncDataIDList!>NULL_PTR<#else>&CanTSyn_SYNCDataIdlist_${timeDomainSlave.shortName}[0]</#if>, /* SyncData ID list */
        <#if timeDomainSlave.parent.canTSynGlobalTimeFupDataIDList!>NULL_PTR<#else>&CanTSyn_FUPDataIdlist_${timeDomainSlave.shortName}[0]</#if>, /* FUP Data ID list */
        CANTSYN_${timeDomainSlave.canTSynRxCrcValidated}, /* configured CRC */
        ${timeDomainSlave.parent.canTSynSynchronizedTimeBaseRef.stbMSynchronizedTimeBaseIdentifier}u, /* STBM Id */
        ${timeDomainSlave.canTSynGlobalTimeFollowUpTimeout/canTSyn_GenerateCfg_c.canTSynGeneral.canTSynMainFunctionPeriod.toInteger()}u, /* Rx follow up timeout in ticks*/
        ${timeDomainSlave.parent.canTSynGlobalTimeDomainId}u, /* domain Id */
        ${timeDomainSlave.canTSynGlobalTimeSequenceCounterJumpWidth}u,  /* jump width */
        <#if timeDomainSlave.parent.canTSynUseExtendedMsgFormat??>"TRUE"<#else>"FALSE"</#if>  /* CanTSynUseExtendedMsgFormat TRUE or FALSE*/

    }
  <#if timeDomainSlave?is_last><#else>,</#if></#list>
};
</#if>

#define CANTSYN_STOP_SEC_CONST_UNSPECIFIED
#include "CanTSyn_MemMap.h"


#define CANTSYN_START_SEC_CONST_UNSPECIFIED
#include "CanTSyn_MemMap.h"


/* CanTSyn config structure */
CONST( CanTSyn_ConfigType, AUTOMATIC) CanTSyn_Config[] = {
{
<#if canTSyn_GenerateCfg_c.isCanTSynGlobalTimeMasterConfigured()??>
    &CanTSyn_Master_Tx_a[0u],	            /* Global Time Domain Master */
</#if><#if canTSyn_GenerateCfg_c.isCanTSynGlobalTimeSlaveConfigured()??>
    &CanTSyn_Slave_Rx_a[0u],	                /* Global Time Domain Slave */
</#if>
    ${canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeMaster().size}u,     /* Total number of configured master */
    ${canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeSlave().size}u       /* Total number of configured slave */
}
};


#define CANTSYN_STOP_SEC_CONST_UNSPECIFIED
#include "CanTSyn_MemMap.h"


#define CANTSYN_START_SEC_VAR_CLEARED_UNSPECIFIED
#include "CanTSyn_MemMap.h"

<#if canTSyn_GenerateCfg_c.isCanTSynGlobalTimeMasterConfigured()??>
/* Global Time Master Pdu status */
VAR(CanTSyn_Tx_Status_t, AUTOMATIC)  CanTSyn_TxRam[${canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeMaster().size}u];
</#if>

<#if canTSyn_GenerateCfg_c.isCanTSynGlobalTimeSlaveConfigured()??>
/* Global Time Slave Pdu status */
VAR(CanTSyn_Rx_Status_t, AUTOMATIC)  CanTSyn_RxRam[${canTSyn_GenerateCfg_c.CanTSyn_getCanTSynGlobalTimeSlave().size}u];
</#if>

#define CANTSYN_STOP_SEC_VAR_CLEARED_UNSPECIFIED
#include "CanTSyn_MemMap.h"



