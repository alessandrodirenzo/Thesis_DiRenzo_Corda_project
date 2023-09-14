import xml.etree.ElementTree as ET
import numpy as np


FileName = "XMLDocuments/FromChorToCorda/BirthCertificateIssueChorDiagram.xml"

mytree = ET.parse(FileName)
myroot = mytree.getroot()

cordadocuments = myroot.get("name") + "CordaTemplate" + ".txt"


def statecontractpair(root):
    state = "State: " + root.get("name")
    contract = "Contract: " + root.get("name") + "Contract"
    with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'w') as f:
        f.write(state + "\n")
        f.write(contract + "\n")
        f.write("---------------------------" + "\n")


statecontractpair(myroot)


def exclgatewayrepresentation(root):
    i = 1
    for x in root.iter("exclusivegateway"):
        attribute_pos = "State.accepted_" + str(i) + "= false"
        attribute_neg = "State.rejected_" + str(i) + "= false"
        i = i + 1
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write(attribute_pos + "\n")
            f.write(attribute_neg + "\n")


exclgatewayrepresentation(myroot)


def addingnodes(root):
    for x in root.iter("participant"):
        participant = "Node " + x.attrib["id"] + ": " + x.text
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write(participant + "\n")
    with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
        f.write("Default node: Notary" + "\n")
        f.write("-------------------------------" + "\n")


addingnodes(myroot)

array = np.array([""], dtype=object)
array1 = np.array([""], dtype=object)
array2 = np.array([""], dtype=object)
array3 = np.column_stack((array, array1, array2))


def workflowscommands(root):
    global array3
    count = 0
    sent = False
    for x in root.iter("choreographytask"):
        workflows_name = x.attrib["name"] + "Flow"
        workflows_name = "".join(workflows_name.split())
        workflows_name = workflows_name.replace("_", "")
        workflows_init = workflows_name + "Initiator"
        workflows_resp = workflows_name + "Responder"
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write("Workflow: " + workflows_name + " .transaction_OutputState = ")
            f.write(workflows_init + " | ")
            f.write(workflows_resp + "\n")
        workflow_outputstate_initiator = workflows_init + "_outputState.initiator = " + x.find("initiator").text
        workflow_outputstate_receiver = workflows_init + "_outputState.receivers = " + x.find("receivers").text
        workflow_transaction_outputstate = workflows_init + ".transaction_outputState = OutputState"
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write(workflow_outputstate_initiator + "\n")
            f.write(workflow_outputstate_receiver + "\n")
        if x.find("incoming") != None:
            wf_transaction = workflows_init + ".transaction_hasIntputState"
            with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
                f.write(wf_transaction + "\n")
        else:
            wf_transaction = workflows_init + ".transaction_hasNoIntputState"
            with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
                f.write(wf_transaction + "\n")
        if "PrivitySharingData" in workflows_name:
            with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
                f.write(workflows_init + ".transaction_Attachment = "+ x.find("message").text + "\n")
        else:
            with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
                f.write(workflows_init + ".transaction_outputState.message = "+ x.find("message").text + "\n")
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write(workflow_transaction_outputstate + "\n" + "\n")
        command = workflows_name.replace("Flow", "")
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write("Contract.Command: " + command + "\n")
            f.write(command + " rule:" + wf_transaction + "\n")
            f.write(command + " rule:" + workflows_init + ".transaction_outputStateType = " +  root.get("name") + "\n")
        if "Assessment" in workflows_name and sent == False:
            sent = True
            with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
                f.write("State.validators_of_second_category = false" + "\n")
        elif "PrivitySharingData" in workflows_name:
            count = count + 1
            with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
                f.write("State.sharingDataPrivity" + str(count) + " = false" + "\n")
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write("-----------------------------" + "\n")


        positionplace = x.attrib["seq"]
        positionplace = positionplace.replace("task_", "")

        sequential = x.find("outgoing")
        if sequential != None:
           taskseq = sequential.text
           taskseq = taskseq.replace("task_", "")
           taskseq = taskseq.replace(" ", "")
        else:
           taskseq= ""
        wfname = np.array([workflows_name], dtype=object)
        posplace = np.array([positionplace], dtype=object)
        taskseqarr = np.array([taskseq], dtype=object)
        multarray = np.column_stack((wfname, posplace,taskseqarr))

        array3 = np.concatenate((array3, multarray))

workflowscommands(myroot)

sent = False

for x in myroot.iter("exclusivegateway"):
    sent = True
    a1 = np.array([x.attrib["name"]],dtype=object)
    a2 = np.array([x.attrib["seq"]], dtype=object)
    string = x.find("outgoing").text
    array = string.split(",")
    var1  = array[0].replace("task_", "")
    var1 = var1.replace(" ", "")
    var2 = array[1].replace("task_", "")
    var2 = var2.replace(" ", "")
    a3 = np.array(var1, dtype=object)
    a4 = np.array(var2, dtype=object)
    array5 = np.column_stack((a1, a2, a3))
    array6 = np.column_stack((a1, a2, a4))
    array3 = np.concatenate((array3, array5))
    array3 = np.concatenate((array3, array6))

array3 = np.delete(array3, (0), axis=0)

def seqorder(array):
    with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
        f.write("Sequential dependencies of tasks: " + "\n" + "\n")
    for i in range(0,np.size(array3, axis=0)):
        workflow = array[i][0]
        if array[i][2] != "":
            workflow = workflow + "->"
            seqpos = array[i][2]
            for j in range(0,np.size(array3, axis=0)):
                seqflow = array[j][1]
                if seqflow == seqpos:
                    workflow = workflow + array[j][0]
                    break
        with open(f"CordaTemplateTextualFiles/{cordadocuments}", 'a') as f:
            f.write(workflow + "\n")

seqorder(array3)
