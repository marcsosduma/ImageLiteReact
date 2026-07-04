'use client'

import {Template, RenderIf, InputText, Button, FieldError, useNotification} from "@/components";
import { useMemo, useState } from "react";
import { LoginForm, createValidationSchema, formScheme } from "./formScheme";
import { useFormik } from "formik";
import { useAuthService } from "@/resources";
import { useRouter } from "next/navigation";
import { Credentials } from "@/resources/user/user.resource";

export default function Login() {

    const [ loading, setLoading ] = useState<boolean>(false);
    const [newUserState, setNewUserState] = useState<boolean>(false);

    const authService = useAuthService();
    const router = useRouter();

    const notification = useNotification();

    const validationSchema = useMemo(
        () => createValidationSchema(newUserState),
            [newUserState]
    );

    const {values, handleChange, handleBlur, handleSubmit, errors} = useFormik<LoginForm>({
        initialValues: formScheme,
        validationSchema: validationSchema,
        onSubmit: onSubmit
    });

    async function onSubmit(values: LoginForm) {
        if(!newUserState) {
            const credentials: Credentials = {email: values.email, password: values.password};
            setLoading(true);
            try {
                const accessToken = await authService.authenticate(credentials);
                authService.initSession(accessToken);
                authService.isSessionValid();
                router.push('/galeria');
            } catch (error: any) {
                const message = error.message || 'An error occurred during authentication.';
                notification.notify(message, "error");
            }
            setLoading(false);
        }else{
            setLoading(true);
            try {
                const user = {email: values.email, password: values.password, name: values.name};
                await authService.save(user);
                notification.notify('User created successfully!', "success");
                setNewUserState(false);
            } catch (error: any) {
                const message = error.message || 'An error occurred during user registration.';
                notification.notify(message, "error");
            }
            setLoading(false);
        }
    }

    return (

        <Template loading={loading}>
            <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">

                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <h2 className="mt-10 text-center text-1xl font-bold leading-9 tracking-tight text-gray-900">
                        {newUserState ? 'Create your account' : 'Login to your account'}
                    </h2>
                </div>

                <div className="mt-5 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form className="space-y-2" onSubmit={handleSubmit}>
                        <RenderIf condition={newUserState}>
                            <div>
                                <label className="block text-sm font-medium leading-6 text-gray-900">Name:</label>
                            </div>
                            <div className="mt-2">
                                <InputText id="name" 
                                            placeholder="Type your name"
                                            style="w-full"
                                            type="text"
                                            value={values.name}
                                            onChange={handleChange}
                                />
                            </div>
                            <FieldError error={errors.name} />
                        </RenderIf>
                        <div>
                            <label className="block text-sm font-medium leading-6 text-gray-900">E-mail:</label>
                        </div>
                        <div className="mt-2">
                            <InputText id="email"
                                        placeholder="Type your e-mail"
                                        style="w-full"
                                        type="email"
                                        value={values.email}
                                        onChange={handleChange}
                            />  
                        </div>
                        <FieldError error={errors.email} />
                        <div>
                            <label className="block text-sm font-medium leading-6 text-gray-900">Password:</label>
                        </div>
                        <div className="mt-2">
                            <InputText id="password"
                                        placeholder="Type your password"
                                        style="w-full"
                                        type="password"
                                        value={values.password}
                                        onChange={handleChange}
                            />
                        </div>
                        <FieldError error={errors.password} />
                        <RenderIf condition={newUserState}>
                            <div>
                                <label className="block text-sm font-medium leading-6 text-gray-900">Confirm Password:</label>
                            </div>
                            <div className="mt-2">
                                <InputText id="passwordMatch"
                                            placeholder="Repeat your password"
                                            style="w-full"
                                            type="password"
                                            value={values.passwordMatch}
                                            onChange={handleChange}
                                />
                            </div>
                            <FieldError error={errors.passwordMatch} />
                        </RenderIf>
                        <div className="flex items-center space-x-2">
                            <RenderIf condition={newUserState}>
                                <Button style="bg-indigo-700 hover:bg-indigo-500 focus-visible:outline-indigo-500"
                                    label="Save"
                                    type="submit"
                                />
                                <Button style="bg-red-700 hover:bg-red-500 focus-visible:outline-red-500"
                                    label="Cancel"
                                    type="submit"
                                    onClick={() => setNewUserState(false)}
                                />
                            </RenderIf>
                            <RenderIf condition={!newUserState}>
                                <Button style="bg-indigo-700 hover:bg-indigo-500 focus-visible:outline-indigo-500"
                                    label="Login"
                                    type="submit"
                                />
                                <Button style="bg-red-700 hover:bg-red-500 focus-visible:outline-red-500"
                                    label="Sign Up"
                                    type="submit"
                                    onClick={() => setNewUserState(true)}
                                />
                            </RenderIf>
                        </div>
                    </form>
                </div>
            </div>
        </Template>
    )
}
