pragma solidity ^0.4.10;

contract EDao {
    //address public owner = 0x82ab63a7314747f1fedc6aebac14474687935f36 ; //test account
    address public constant owner = 0x86ab01ddb2be0b5ecdbe9a5095aa407086fafa29; //change

    //Events
    event ReturnValue(uint256 ret);
    event Success(address src, uint256 ret);
    event Fail(address src, uint256 ret);
    event NotEnoughFunds(address src, uint256 req, uint256 avail, uint256 balance);
    event RecievedFunds(uint256 v);

    //Structs 
    struct Fund {
        address payoutAddr;
        uint256 amount;
    }

    struct Investor {
        bool canFund;
        bool canAddInvestor;
    }

    //Mappings  
    mapping(address => Investor) investors;
    mapping(address => Fund) funds;

    //constructor
    function EDao() payable {
        // Set the initial owner as one of the investors
        investors[owner] = Investor({canFund : true, canAddInvestor : true});

        // Testing ignore that
        investors[msg.sender] = Investor({canFund : true, canAddInvestor : true});
    }

    function fundit(address to) payable {
        Investor b = investors[msg.sender];
        if (b.canFund) {
            Fund f = funds[to];
            f.payoutAddr = to;
            f.amount += msg.value;
            Success(msg.sender, 0);
        } else {
            Fail(msg.sender, 1);
            throw;
        }
    }

    function queryFunds(address addr) returns (uint256) {
        Fund f = funds[addr];
        ReturnValue(f.amount);
        return f.amount;
    }

    function withdraw(address addr, uint256 amount) returns (bool) {
        Fund f = funds[addr];
        if (f.amount >= amount && amount <= this.balance) {
            if (f.payoutAddr.call.value(amount)()) {
                f.amount = f.amount - amount;
                return true;
            }
        }
        NotEnoughFunds(msg.sender, amount, f.amount, this.balance);
        return false;
    }

    // Helper function to check if looking at the right contract.
    // Executes a call to contract locally without sending a tx:
    // 
    // > cinstance.whoami.call({from:eth.accounts[0]})
    //  "owner"
    // > cinstance.whoami.call({from:eth.accounts[1]})
    //  "not owner"
    //
    // If not "owner" then check if you have used the right account
    // > personal.listAccounts.indexOf(cinstance.owner()) > -1
    //  true
    function whoami() constant returns (string){
        if (msg.sender == owner) {
            Success(msg.sender, 2);
            return "owner";
        } else {
            Fail(msg.sender, 3);
            return "not owner";
        }
    }

    function addInvestor(address investorAddr, bool canAdd) {
        Investor b = investors[msg.sender];
        if (b.canAddInvestor) {
            Investor memory newb = Investor({canFund : true, canAddInvestor : canAdd});
            investors[investorAddr] = newb;
        } else {
            Fail(msg.sender, 5);
        }
    }

    function getInvestor(address investorAddr) constant returns (bool canFund, bool canAddInvestor) {
        Investor b = investors[investorAddr];
        canFund = b.canFund;
        canAddInvestor = b.canAddInvestor;
    }

    //function() payable {
    //    RecievedFunds(msg.value);
    //}   
}


