package net.pawelhajduk.daggerdemo;

import net.pawelhajduk.daggerdemo.api.model.Repository;

import java.util.Arrays;
import java.util.List;

public class Mocks {

    //Here we put mocked object which will be used in tests.

    public static final List<Repository> MOCKED_LIST = Arrays.asList(new Repository("element 0", 3, 3),
                                                                     new Repository("element 1", 3, 3),
                                                                     new Repository("element 2", 3, 3),
                                                                     new Repository("element 3", 3, 3));

    public static final List<Repository> MOCKED_LIST_ALT = Arrays.asList(new Repository("element 6", 3, 3),
                                                                     new Repository("element 7", 3, 3),
                                                                     new Repository("element 8", 3, 3),
                                                                     new Repository("element 9", 3, 3));
}
