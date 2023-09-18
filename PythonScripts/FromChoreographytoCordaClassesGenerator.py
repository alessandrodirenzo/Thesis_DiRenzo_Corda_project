import xml.etree.ElementTree as ET

import re

from numpy.core.defchararray import lower

FileName = "XMLDocuments/FromChorToCorda/BirthCertificateIssueChorDiagram.xml"

mytree = ET.parse(FileName)
myroot = mytree.getroot()


def participants(root):
    state = root.get("name")
    for x in root.iter("participant"):
        participant = x.text
        node_body = """node {
        name "O=""" + participant + """,L=Milan,C=IT"
        p2pPort 10005
        rpcSettings {
            address("localhost:10006")
            adminAddress("localhost:10046")
        }
        rpcUsers = [[ user: "user1", "password": "test", "permissions": ["ALL"]]]
    }
    """
        with open(f"{state}/nodes configuration.txt", 'a') as f:
            f.write(node_body)
    notary = """
     node {
        name "O=Notary,L=Milan,C=IT"
        notary = [validating : false]
        p2pPort 10002
        rpcSettings {
            address("localhost:10003")
            adminAddress("localhost:10043")
        }
    }
    """
    with open(f"{state}/nodes configuration.txt", 'a') as f:
        f.write(notary)


participants(myroot)


def statecontractworkflow(root):
    state = root.get("name")
    contract = state + "Contract"
    i = 1
    z = 1
    v = 1
    s = 1
    p = 1
    y = 1
    workflows = []
    attrib_p = ""
    string_param = ""
    string_param2 = ""
    string_p = ""
    string_param3 = ""
    boolean = "boolean"
    getter_method = ""
    inputAttrib = "idState"
    for x in root.iter("exclusivegateway"):
        attribute_pos = "accepted" + str(i)
        attribute_neg = "rejected" + str(i)
        first_cat = "firstcategory" + str(i)
        i = i + 1
        if attribute_pos != "":
            string_param = string_param + boolean + " " + attribute_pos + ";" + "\n      " + boolean + " " + attribute_neg + ";" + "\n" + boolean + " " + first_cat + ";" + "\n"
            string_param2 = string_param2 + ", " + boolean + " " + attribute_pos + ", " + boolean + " " + attribute_neg + ", " + boolean + " " + first_cat
            string_p = string_p + ", false , false , false"
            string_param3 = string_param3 + "this." + attribute_pos + " = " + attribute_pos + ";" + "\n     " + "this." + attribute_neg + " = " + attribute_neg + ";" + "\n     " + "this." + first_cat + " = " + first_cat + ";" + "\n     "
            getter_method = getter_method + """
                    public """ + boolean + " get" + attribute_pos + """(){
                        return """ + attribute_pos + """;
                    }
                    
                    public """ + boolean + " get" + attribute_neg + """(){
                        return """ + attribute_neg + """;
                    }
                    
                    public """ + boolean + " get" + first_cat + """(){
                        return """ + first_cat + """;
                    }
            """

    class_body = """
        UniqueIdentifier idState;""" + "\n    " + string_param + """
        String message;
        Party initiator; 
        List<Party> receivers;
        """
    class_body2 = "\n             " + """
        public {{state}}(UniqueIdentifier idState, String message, Party initiator, List<Party> receivers"""
    class_body3 = """){
            this.idState = idState;
            this.message = message;
            this.initiator = initiator;
            this.receivers = receivers;""" + "\n        "
    class_body4 = "}"

    class_body5 = """
                
                public UniqueIdentifier getidState(){
                    return idState; 
                }
                
                public String getmessage(){
                    return message;
                }
                public Party getinitiator(){
                    return initiator;
                }
                public List<Party> getreceivers(){
                    return receivers;
                }
                  """

    class_body6 = """
    
        @Override
        public List<AbstractParty> getParticipants() {
            List<AbstractParty> entities= new ArrayList<AbstractParty>();
            entities.add(initiator);
            entities.addAll(receivers);
            return entities;
    }"""

    class_body2 = class_body2.replace("{{state}}", state)

    attachment = ""
    contract_command = ""
    contract_requirements = ""
    for x in root.iter("choreographytask"):
        workflows_name = x.attrib["name"] + "Flow"
        if x.attrib["name"] == "Privity_SharingData":
            workflows_name = workflows_name + str(z)
        if x.attrib["name"] == "Decision_SharingData":
            workflows_name = workflows_name + str(v)
        if x.attrib["name"] == "Acceptance_Assessment":
            workflows_name = workflows_name + str(s)
            s = s + 1
        if x.attrib["name"] == "Rejection_Assessment":
            workflows_name = workflows_name + str(y)
            y = y + 1
        workflows_name = "".join(workflows_name.split())
        workflows_name = workflows_name.replace("_", "")
        workflows_init = workflows_name + "Initiator"
        workflows_resp = workflows_name + "Responder"

        workflows_body = """
            @InitiatingFlow
            @StartableByRPC
            public static class {{workflows_init}} extends FlowLogic<SignedTransaction> {
            
            private Party initiator;
            private List<Party> receivers;
            
            public """ + workflows_init + """( List<Party> receivers) {
            this.receivers = receivers;
            }
            
            @Override
            @Suspendable
            public SignedTransaction call() throws FlowException {
            
            this.initiator = getOurIdentity();
            
            final Party notary = getServiceHub().getNetworkMapCache().getNotary(CordaX500Name.parse(\"O=Notary,L=Milan,C=IT\")); 
            
            """

        outputState = """ 
                {{state}} output = new {{state}}( """ + inputAttrib + """, message, initiator, receivers """

        workflows_body1 = """
                  
            final TransactionBuilder builder = new TransactionBuilder(notary);
            
            builder.addOutputState(output);
            """

        workflows_body2 = """
            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);
            
            ArrayList<AbstractParty> parties = new ArrayList<>();
            parties = (ArrayList) output.getParticipants();
            
            List<FlowSession> signerFlows = parties.stream()
                    .filter(it -> !it.equals(getOurIdentity()))
                    .map(this::initiateFlow)
                    .collect(Collectors.toList());
        

            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(ptx, signerFlows, CollectSignaturesFlow.Companion.tracker()));
                    
            return subFlow(new FinalityFlow(fullySignedTx, signerFlows));
                            
            }
            }
            
            @InitiatedBy({{workflows_name}}.{{workflows_init}}.class)
            public static class {{workflows_resp}} extends FlowLogic<Void>{
                 
            private FlowSession counterpartySession;
    
            //Constructor
            public {{workflows_resp}}(FlowSession counterpartySession) {
                this.counterpartySession = counterpartySession;
            }
    
            @Suspendable
            @Override
            public Void call() throws FlowException {
    
                class SignTxFlow extends SignTransactionFlow {
                    private SignTxFlow(FlowSession otherPartyFlow) {
                        super(otherPartyFlow);
                    }
                    @Override
                    protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {
                   
                    }
    
    
                }
             
                final SignTxFlow signTxFlow = new SignTxFlow(counterpartySession);
                final SecureHash txId = subFlow(signTxFlow).getId();
    
                subFlow(new ReceiveFinalityFlow(counterpartySession, txId));
                return null;
                 }
            } 
        
        """

        workflows_body = workflows_body.replace("{{workflows_name}}", workflows_name)
        workflows_body = workflows_body.replace("{{workflows_init}}", workflows_init)
        outputState = outputState.replace("{{state}]", state)

        workflows_body1 = workflows_body1.replace("{{workflows_resp}}", workflows_resp)
        workflows_body1 = workflows_body1.replace("{{state}}", state)
        command = workflows_name.replace("Flow", "")
        contract_command = contract_command + "class " + command + " implements Commands {}" + " \n            "

        if x.find("incoming") is not None:
            inputAttrib = "input.getidState()"
            inputstate = state + " input = tx.inputsOfType(" + state + ".class).get(0);"
            require_1 = "require.using(\"input state present\", !tx.getInputStates().isEmpty());"
            retrieve_input = """
             Vault.Page<{{state}}> results = getServiceHub().getVaultService()
                    .queryBy({{state}}.class);

            List<StateAndRef<{{state}}>> states = results.getStates();

            final StateAndRef inputState = states.get(0);

            final {{state}} input= ({{state}}) inputState.getState().getData();

            Party notary = states.get(0).getState().getNotary();
            """
            transaction_input = "builder.addInputState(inputState);"

        else:
            inputstate = ""
            require_1 = "require.using(\"input state not present\", tx.getInputStates().isEmpty());"
            retrieve_input = "final UniqueIdentifier idState = new UniqueIdentifier();"
            transaction_input = ""

        if x.attrib["name"] == "Privity_SharingData":
            require_2 = ""
            message = "\n         " + "String message =\"\"" + ";\n         "
            attrib = "datashared" + str(z)
            attrib_2 = ", " + boolean + " datashared" + str(z)
            attrib_p = attrib_p + ", false"
            attrib_3 = "this.datashared" + str(z) + " = " + "datashared" + str(z) + ";" + "\n        "
            z = z + 1
            getter_method = getter_method + """ public """ + boolean + " get" + attrib + """(){
                        return """ + attrib + """;
                    }
                 """
            attrib = boolean + " " + attrib + ";"
            class_body = class_body + "\n          " + attrib
            string_param2 = string_param2 + attrib_2
            string_param3 = string_param3 + attrib_3
            attachment = """
                 SecureHash attachmentHash = null;
            try {
                attachmentHash = SecureHash.parse(uploadAttachment(
                        pathSource,
                        getServiceHub(),
                        getOurIdentity(),
                        "nomefile")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            """
            transaction_add = "builder.addAttachment(attachmentHash);"

        elif x.attrib["name"] == "Decision_SharingData":
            require_2 = ""
            message = "\n         " + "String message = \"\"" + ";\n         "
            attrib = "decisiondatashared" + str(v)
            attrib_2 = ", " + boolean + " decisiondatashared" + str(v)
            attrib_p = attrib_p + ", false"
            attrib_3 = "this.decisiondatashared" + str(v) + " = " + "decisiondatashared" + str(v) + ";" + "\n        "
            v = v + 1
            getter_method = getter_method + """ 
            public """ + boolean + " get" + attrib + """(){
                return """ + attrib + """;
            }
            """
            attrib = boolean + " " + attrib + ";"
            class_body = class_body + "\n          " + attrib
            string_param2 = string_param2 + attrib_2
            string_param3 = string_param3 + attrib_3
            attachment = """
                            SecureHash attachmentHash = null;
                       try {
                           attachmentHash = SecureHash.parse(uploadAttachment(
                                   pathSource,
                                   getServiceHub(),
                                   getOurIdentity(),
                                   "nomefile")
                           );
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       """
            transaction_add = "builder.addAttachment(attachmentHash);"

        elif "Acceptance_Assessment" in x.attrib["name"]:
            require_2 = "require.using(\"Message not null\", !output.getMessage().equals(\"\"));"
            message = "\n         " + "String message = \"prova\"" + ";\n         "
            secondcategory = "secondcategory" + str(p)
            p = p + 1
            getter_method = getter_method + """ 
                public """ + boolean + " get" + secondcategory + """(){
                    return """ + secondcategory + """;
                }
                """
            attrib = boolean + " " + secondcategory + ";"
            attrib_2 = ", " + boolean + " secondcategory"
            attrib_p = attrib_p + ", false"
            attrib_3 = "this." + secondcategory + " = " + secondcategory + ";" + "\n        "
            class_body = class_body + "\n          " + attrib
            string_param2 = string_param2 + attrib_2
            string_param3 = string_param3 + attrib_3
            attachment = ""
            transaction_add = ""

        else:
            require_2 = "require.using(\"Message not null\", !output.getMessage().equals(\"\"));"
            message = "\n         " + "String message = \"prova\"" + ";\n         "
            attrib_p = attrib_p + ""
            attachment = ""
            transaction_add = ""
        outputState = outputState + string_p + attrib_p + ");" + "\n            "
        contract_requirements = contract_requirements + """ 
            if (commandData instanceof Commands.""" + command + """) {
                  {{state}} output = tx.outputsOfType({{state}}.class).get(0);
                  """ + "\n             " + inputstate + """

                   requireThat(require -> {
                        """ + require_1 + "\n             " + require_2 + """
                        return null;
                   });
                   
            }
         """
        transaction_input = transaction_input + "\n         " + "builder.addCommand(new {{contract}}.Commands.{{command}}(), Arrays.asList(this.initiator.getOwningKey(), this.receivers.get(0).getOwningKey()));" + "\n         " + transaction_add;
        transaction_input = transaction_input.replace("{{command}}", command)
        transaction_input = transaction_input.replace("{{contract}}", contract)
        if retrieve_input is not None:
            retrieve_input = retrieve_input.replace("{{state}}", state)
        if attachment is not None:
            outputState1 = outputState + "\n           " + attachment
        else:
            outputState1 = outputState
        outputState = outputState.replace("{{state}}", state)
        outputState1 = outputState1.replace("{{state}}", state)
        workflows_body2 = workflows_body2.replace("{{workflows_name}}", workflows_name);
        workflows_body2 = workflows_body2.replace("{{workflows_init}}", workflows_init);
        workflows_body2 = workflows_body2.replace("{{workflows_resp}}", workflows_resp);
        java_workflows_code = workflows_body + retrieve_input + message + outputState1 + workflows_body1 + transaction_input + workflows_body2
        workflows.append(workflows_name)
        with open(f"{state}/{workflows_name}.java", "w") as java_file:
            java_file.write(f"public class {workflows_name}{{\n{java_workflows_code}\n}}")

    java_code = class_body + class_body2 + string_param2 + class_body3 + string_param3 + class_body4 + class_body5 + getter_method + class_body6
    with open(f"{state}/{state}.java", "w") as java_file:
        java_file.write(f"public class {state} implements ContractState{{\n{java_code}\n}}")

    contract_lower = contract.lower()
    contract_body = """
                     public static final String ID = \"com.template.contracts.{{contract}}\";
                     @Override
                     public void verify(LedgerTransaction tx) {

                     final CommandData commandData = tx.getCommands().get(0).getValue();
                     
                      """ + contract_requirements + """
                
                    }
                    public interface Commands extends CommandData {               
                    """ + contract_command + """
                }
            """
    contract_body = contract_body.replace("{{command}}", command)
    contract_body = contract_body.replace("{{state}}", state)
    contract_body = contract_body.replace("{{contract}}", contract)
    with open(f"{state}/{contract}.java", "w") as java_file:
        java_file.write(f"public class {contract} implements Contract{{\n{contract_body}\n}}")

    for index, workflow in enumerate(workflows):

        with open(f"{state}/{workflow}.java", "r") as java_file:
            java_code = java_file.read()

        search_variable = state + " output"
        pattern = re.compile(re.escape(search_variable) + r'.+?\;')

        outputState_change = outputState.replace("input.getidState()", "idState")

        if index == 0:
            new_java_code = pattern.sub(outputState_change, java_code)
        else:
            new_java_code = pattern.sub(outputState, java_code)

        with open(f"{state}/{workflow}.java", "w") as java_file:
            java_file.write(new_java_code)


statecontractworkflow(myroot)
