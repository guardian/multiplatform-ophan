namespace * ophan.thrift.event
namespace cocoa GLAOphanThriftEvent

enum Product {

    /**
    * A one-off contribution
    **/
    CONTRIBUTION = 1

    /**
    * A recurring contribution
    **/
    RECURRING_CONTRIBUTION = 2

    /**
    * A Membership Supporter signup
    **/
    MEMBERSHIP_SUPPORTER = 3

    /**
    * A Membership Patron signup
    **/
    MEMBERSHIP_PATRON = 4

    /**
    * A Membership Partner signup
    **/
    MEMBERSHIP_PARTNER = 5

    /**
    * A digital subscription
    **/
    DIGITAL_SUBSCRIPTION = 6

    /**
    * DEPRECATED DO NOT USE
    **/
    PAPER_SUBSCRIPTION_EVERYDAY = 7

    /**
    * DEPRECATED DO NOT USE
    **/
    PAPER_SUBSCRIPTION_SIXDAY = 8

    /**
    * DEPRECATED DO NOT USE
    **/
    PAPER_SUBSCRIPTION_WEEKEND = 9

    /**
    * DEPRECATED DO NOT USE
    **/
    PAPER_SUBSCRIPTION_SUNDAY = 10

    /**
    * A paper subscription
    **/
    PRINT_SUBSCRIPTION = 11

    /**
    * An app premium tier signup
    **/
    APP_PREMIUM_TIER = 12

}