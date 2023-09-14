import xml.etree.ElementTree as ET

FileName = "./HospitalTelevisitExternalwithSecNot.xml"

mytree = ET.parse(FileName)
myroot = mytree.getroot()

contract = myroot.get("name") + "AdditionalTasksComp" + ".xml"


root = ET.Element("choreographydiagram", name = myroot.get("name"))
root1 = ET.Element("choreographytasks")

for x in myroot.findall("./choreographytasks/choreographytask/dataobject"):
    new_do_task = ET.Element("choreographytask", name='Privity_SharingData')
    tag_1 = ET.SubElement(new_do_task, "message")
    tag_1.text = x.text
    tag_2 = ET.SubElement(new_do_task, "initiator")
    tag_2.text = x.attrib["owner"]
    tag_3 = ET.SubElement(new_do_task, "receivers")
    tag_3.text = x.attrib["others"]
    root1.append(new_do_task)

for x in myroot.findall("./choreographytasks/exclusiveGateway/enforceability_of_decision"):
    if x.attrib["requiresharingdata"] == "yes":
        new_do_task = ET.Element("choreographytask", name='Decision_SharingData')
        tag_1 = ET.SubElement(new_do_task, "message")
        tag_1.text = x.attrib["data_sharing"]
        tag_2 = ET.SubElement(new_do_task, "initiator")
        tag_2.text = x.attrib["sharing_owner"]
        tag_3 = ET.SubElement(new_do_task, "receivers")
        tag_3.text = x.attrib["other_validators"]
        root1.append(new_do_task)

for x in myroot.findall("./choreographytasks/exclusiveGateway/enforceability_of_decision"):
    new_enf_task_pos = ET.Element("choreographytask", name='Acceptance_Assessment')
    tag_1 = ET.SubElement(new_enf_task_pos, "message")
    tag_1.text = "Acceptation"
    tag_2 = ET.SubElement(new_enf_task_pos, "initiator")
    tag_2.text = x.attrib["initiator_positive"]
    tag_3 = ET.SubElement(new_enf_task_pos, "receivers")
    tag_3.text = x.attrib["receivers_positive"]
    root1.append(new_enf_task_pos)
    new_enf_task_neg = ET.Element("choreographytask", name='Rejection_Assessment')
    tag_1 = ET.SubElement(new_enf_task_neg, "message")
    tag_1.text = "Refusal"
    tag_2 = ET.SubElement(new_enf_task_neg, "initiator")
    tag_2.text = x.attrib["initiator_negative"]
    tag_3 = ET.SubElement(new_enf_task_neg, "receivers")
    tag_3.text = x.attrib["receivers_negative"]
    root1.append(new_enf_task_neg)

root.append(root1)

with open(contract, "w") as files:
    files.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")

root2 = ET.Element("participants")
for x in myroot.iter('participant'):
    part = ET.Element('participant', id = x.attrib["id"])
    part.text = x.text
    root2.append(part)

root.append(root2)

tree = ET.ElementTree(root)
with open(contract, "ab") as files:
    tree.write(files)
