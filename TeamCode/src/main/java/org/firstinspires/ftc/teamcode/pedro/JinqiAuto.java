double shooterkp = 0.005;
double shooterprop = 0;
double shooterd = 0.005;
double shooterPE = 0;
double shooterkd = -0.002;
double shooterES = 0;
double shootertarget = 1420;
double shootererror = 0;
double leavePercent = 0.99;

private void shoot(double ms) {
    shootertarget = 1420;
    blocker.setPosition(1); // open blocker while shooting

    double end = System.currentTimeMillis() + ms;
    while (System.currentTimeMillis() < end) {
        index.setVelocity(800);
        transfer.setVelocity(200);
        intake.setVelocity(2000);
        shootererror = shootertarget - shooter.getVelocity();
        shooterprop = shootererror * shooterkp;
        shooterd = (shooterPE - shootererror) * shooterkd;
        shooterPE = shootererror;
        shooterES += shootererror;
        shooter.setPower(shooterprop + shooterd + (shooterES * 0.00003));
        telemetry.addData("shooterpower: ", shooter.getPower());
        telemetry.addData("shooterror: ", shootererror);
        telemetry.update();
        follower.update();
    }

    turnoff();
}

private void shooterTune() {
    shootererror = shootertarget - shooter.getVelocity();
    shooterprop = shootererror * shooterkp;
    shooterd = (shooterPE - shootererror) * shooterkd;
    shooterPE = shootererror;
    shooterES += shootererror;
    shooter.setPower(shooterprop + shooterd + (shooterES * 0.00003));
}

private void waitms(double ms) {
    index.setVelocity(0);
    transfer.setVelocity(0);
    intake.setVelocity(2000);
    double end = System.currentTimeMillis() + ms;
    while (System.currentTimeMillis() < end) {
        follower.update();
    }
    turnoff();
}

private void turnoff() {
    transfer.setVelocity(0);
    index.setVelocity(100);
}